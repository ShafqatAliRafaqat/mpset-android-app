package com.mpset.pokerevents.Fragments


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mpset.pokerevents.R


class JoinMaps : Fragment(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val  view =  inflater.inflate(R.layout.fragment_join_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val height = 160
        val width = 160
        val bitmapdraw = resources.getDrawable(R.drawable.defaulticon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        for (i in 0 until  5) {

            val dhaka = LatLng(30.6682, 73.111+i)
             mMap?.let {
//                 val url =  URL("https://www.gettyimages.com/gi-resources/images/CreativeLandingPage/HP_Sept_24_2018/CR3_GettyImages-159018836.jpg")
//                 val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                it.addMarker(MarkerOptions().position(dhaka).title("Marker in Sahiwal"+i))
//                        .setIcon(BitmapDescriptorFactory.fromBitmap(image))
                 it.moveCamera(CameraUpdateFactory.newLatLng(dhaka))
//                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(3.6682, 7.111), 9.0f))
            }
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(30.6682, 73.111), 5.0f))
        mMap.setOnMarkerClickListener(object :GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                Toast.makeText(context,p0!!.id.toString(),Toast.LENGTH_LONG).show()
                return true
            }

            override fun onMapClick(p0: LatLng?) {
            }

        })

//        mMap?.addMarker( MarkerOptions()
//            .position( LatLng(37.4629101,-122.2449094))
//            .title("Iron Man")
//            .snippet("His Talent : Plenty of money"))
//        mMap?.addMarker( MarkerOptions()
//            .position( LatLng(39.4219999, -152.0862462))
//            .title("Spider Man")
//            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)))
    }

}
