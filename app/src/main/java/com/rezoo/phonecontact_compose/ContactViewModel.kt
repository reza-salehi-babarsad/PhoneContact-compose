package com.rezoo.phonecontact_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ContactViewModel(
    private val dao: ContactDao
) :ViewModel(){
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    private val _contacts = _sortType
        .flatMapConcat {  sortType ->
            when(sortType){
                SortType.FIRST_NAME -> dao.getContactsOrderedByName()
                SortType.LAST_NAME -> dao.getContactsOrderedByLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderedByPhoneNumber()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        }
    private val  _state = MutableStateFlow(ContactState())
    //combine states to one state
    val state = combine(_state,_sortType,_contacts){ state,sortTye,contacts ->
        state.copy(
            contacts =contacts,
            sortType = sortTye
        )
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000L), ContactState())

    fun onEvent(event: ContactEvent){
        when(event){
            is ContactEvent.DeleteContact ->{
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }

            }
            ContactEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingContact = false
                )

                }
            }
            ContactEvent.SaveContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber

                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return
                }
                // add contact
                val contact = Contact(firstName=firstName, lastName = lastName,phoneNumber=phoneNumber)
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                // hide dialog after we added a contact and reset our text fields to empty
                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastName = "",
                        phoneNumber = ""
                    )
                }

            }
            is ContactEvent.SetFirstName -> {
                _state.update { it.copy(
                    firstName = event.firstName
                )
                }
            }
            is ContactEvent.SetLastName -> {
                _state.update { it.copy(
                    lastName = event.lastName
                )
                }
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update { it.copy(
                    phoneNumber = event.phoneNumber
                )
                }
            }
            ContactEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingContact = true
                )
                }
            }
            is ContactEvent.SortContacts -> {
                _sortType.value=event.sortType
            }

        }
    }
    
}