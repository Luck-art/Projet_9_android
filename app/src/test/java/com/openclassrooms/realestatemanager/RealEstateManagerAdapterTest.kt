package com.openclassrooms.realestatemanager;



import android.view.ViewGroup
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.tables.RealEstate

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito.*

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class RealEstateManagerAdapterTest {

    private lateinit var adapter: RealEstateManagerAdapter

    @Mock
    private lateinit var mockData: List<RealEstate>

    @Before
    fun setUp() {
        mockData = listOf()
        adapter = RealEstateManagerAdapter(
            dataSet = mockData,
            items = mockData,
            onItemClicked = { _, _ -> }
        ) { _ -> }
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getItemCount_returnsCorrectSize() {
        assertEquals(mockData.size, adapter.itemCount)
    }

    @Test
    fun onCreateViewHolder_createsViewHolder() {
        val parent = mock(ViewGroup::class.java)
        val viewHolder = adapter.onCreateViewHolder(parent, 0)
        assertNotNull(viewHolder)
        assertNotNull(viewHolder.estateImageView)
    }

    @Test
    fun onBindViewHolder_bindsDataCorrectly() {
        val mockEstate = RealEstate(description = "", id = 1, img = "", name = "", address = "", price = 120000, latitude = 2.0, longitude = 2.0, sended = false)
        val viewHolder = mock(RealEstateManagerAdapter.ViewHolder::class.java)
        adapter.onBindViewHolder(viewHolder, 0)
        assertEquals(mockEstate.name, viewHolder.estateTextView.text.toString())
    }

    @Test
    fun onClick_onItem_callsOnItemClicked() {
        val mockEstate = RealEstate(description = "", id = 1, img = "", name = "", address = "", price = 120000, latitude = 2.0, longitude = 2.0, sended = false)
        val viewHolder = adapter.onCreateViewHolder(mock(ViewGroup::class.java), 0)
        viewHolder.itemView.performClick()
        verify(adapter.onItemClicked).invoke(mockEstate, false)
    }

    @Test
    fun filterItems_filtersCorrectly() {
        val searchText = "recherche"
        adapter.filterItems(searchText)
        val filteredItems = adapter.filteredDataSet
        assertTrue(filteredItems.all { it.name.contains(searchText, ignoreCase = true) })
    }


}

