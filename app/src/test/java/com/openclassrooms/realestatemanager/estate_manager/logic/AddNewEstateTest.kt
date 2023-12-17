package com.openclassrooms.realestatemanager.estate_manager.logic

import RealEstateManagerViewModel
import android.location.Address
import androidx.appcompat.app.AlertDialog
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify


class AddNewEstateTest {

    @Test
    fun `create NewEstate - working`() {
        // Given
        val viewModel = mock<RealEstateManagerViewModel>()
        val dialog: AlertDialog = mock<AlertDialog>()
        val existingRealEstate: RealEstate? = null

        val addNewEstate = AddNewEstate()

        // When
        addNewEstate.createNewEstate(
            img = "https://www.google.com",
            name = "the_name",
            description = "description",
            address = "the_address",
            price = 1_000,
            getFromLocationName = {
                listOf(
                    mock<Address>().apply {
                        given(this.latitude).willReturn(10.0)
                        given(this.longitude).willReturn(20.0)
                    }
                )
            },
            isOnSale = false,
            realEstate = existingRealEstate,
            viewModel = viewModel,
            rooms = 6,
            surface = 250.0,
            dialog = dialog,
        )

        // Then
        verify(viewModel).addNewRealEstate(argThat {
            this.name == "the_name" &&
                    this.address == "the_address" &&
                    this.latitude == 10.0 &&
                    this.longitude == 20.0
        })
        verify(dialog).dismiss()
    }


    @Test
    fun `create NewEstate - image is null - should not accept`() {
        // Given
        val viewModel = mock<RealEstateManagerViewModel>()
        val dialog: AlertDialog = mock<AlertDialog>()
        val existingRealEstate: RealEstate? = null

        val addNewEstate = AddNewEstate()

        // When
        addNewEstate.createNewEstate(
            img = null, // <-----
            name = "the_name",
            description = "description",
            address = "the_address",
            price = 1_000,
            getFromLocationName = {
                listOf(
                    mock<Address>().apply {
                        given(this.latitude).willReturn(10.0)
                        given(this.longitude).willReturn(20.0)
                    }
                )
            },
            isOnSale = false,
            realEstate = existingRealEstate,
            viewModel = viewModel,
            rooms = 5,
            surface = 250.0,
            dialog = dialog,
        )

        // Then
        verify(viewModel, never()).addNewRealEstate(any())
        verify(dialog, never()).dismiss()
    }

    @Test
    fun `create NewEstate - adress is empty - should not accept`() {
        // Given
        val viewModel = mock<RealEstateManagerViewModel>()
        val dialog: AlertDialog = mock<AlertDialog>()
        val existingRealEstate: RealEstate? = null

        val addNewEstate = AddNewEstate()

        // When
        addNewEstate.createNewEstate(
            img = "https://www.google.com",
            name = "the_name",
            description = "description",
            address = "the_address",
            price = 1_000,
            getFromLocationName = {
                emptyList() // <-----
            },
            isOnSale = false,
            realEstate = existingRealEstate,
            viewModel = viewModel,
            rooms = 8,
            surface = 300.0,
            dialog = dialog,
        )

        // Then
        verify(viewModel, never()).addNewRealEstate(any())
        verify(dialog, never()).dismiss()
    }

    @Test
    fun `edit Real Estate`() {
        // Given
        val viewModel = mock<RealEstateManagerViewModel>()
        val dialog: AlertDialog = mock<AlertDialog>()
        val existingRealEstate: RealEstate = RealEstate(
            id = 1234,
            img = "https://www.google.com",
            name = "the_name",
            description = "description",
            address = "the_address",
            price = 1_000,
            latitude = 10.0,
            longitude = 20.0,
            rooms = 6,
            surface = 250.0,
            sended = false

        )

        val addNewEstate = AddNewEstate()

        // When
        addNewEstate.createNewEstate(
            img = "https://www.google.com",
            name = "new_name",
            description = "description",
            address = "the_address",
            price = 1_000,
            getFromLocationName = {
                listOf(
                    mock<Address>().apply {
                        given(this.latitude).willReturn(10.0)
                        given(this.longitude).willReturn(20.0)
                    }
                )
            },
            isOnSale = false,
            realEstate = existingRealEstate,
            viewModel = viewModel,
            rooms = 6,
            surface = 250.0,
            dialog = dialog,
        )

        // Then
        verify(viewModel).editRealEstate(argThat {
            this.id == 1234L &&
                    this.address == "the_address" &&
                    this.name == "new_name"
        })
        verify(viewModel, never()).addNewRealEstate(any())
        verify(dialog).dismiss()
    }
}