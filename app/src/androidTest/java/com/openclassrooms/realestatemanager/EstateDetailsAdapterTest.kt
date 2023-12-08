package com.openclassrooms.realestatemanager

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.adapters.EstateDetailsAdapter
import com.openclassrooms.realestatemanager.database.tables.Media
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Arrays


class EstateDetailsAdapterTest {

    @Before
    fun setUp() { //init mock
        val mockLayoutInflater: LayoutInflater = Mockito.mock(LayoutInflater::class.java)
        val mockParent: ViewGroup = Mockito.mock(ViewGroup::class.java)
        val mockView: View = Mockito.mock(View::class.java)
        val mockImageView: ImageView = Mockito.mock(ImageView::class.java)
        val mockVideoView: VideoView = Mockito.mock(VideoView::class.java)
        val mockGlide: Glide = Mockito.mock(Glide::class.java)

        Mockito.`when`(mockLayoutInflater.inflate(anyInt(), eq(mockParent), eq(false))).thenReturn(mockView)
        Mockito.`when`(mockView.findViewById(R.id.thumbnail_image)).thenReturn(mockImageView)
        Mockito.`when`(mockView.findViewById(R.id.thumbnail_video)).thenReturn(mockVideoView)
    }

    @Test
    fun addMediaItem_addsItemAndNotifies() {
        val adapter = EstateDetailsAdapter(ArrayList())
        val newMedia = Media(description = "", id = 0, uri = "", realEstateId = 0, type = "")
        adapter.addMediaItem(newMedia)
        assertEquals(1, adapter.itemCount)
        Mockito.verify(adapter).notifyItemInserted(anyInt())
    }

    @Test
    fun updateMediaItems_updatesListCorrectly() {
        val adapter = EstateDetailsAdapter(ArrayList())
        val newMediaItems = Arrays.asList(Media(description = "", id = 0, uri = "", realEstateId = 0, type = ""), Media(description = "", id = 1, uri = "", realEstateId = 1, type = ""))
        adapter.updateMediaItems(newMediaItems)
        assertEquals(newMediaItems.size, adapter.itemCount)
    }

    @Test
    fun setOnItemClickedListener_triggersCorrectly() {
        val adapter = EstateDetailsAdapter(ArrayList())
        val testMedia = Media(description = "", id = 1, uri = "", realEstateId = 1, type = "")

        val mockListener: EstateDetailsAdapter.OnItemClickedListener = Mockito.mock(
            EstateDetailsAdapter.OnItemClickedListener::class.java
        )
        adapter.setOnItemClickedListener(mockListener)

        adapter.onItemClicked(testMedia)

        Mockito.verify(mockListener).onItemClicked(eq(testMedia))
    }


}