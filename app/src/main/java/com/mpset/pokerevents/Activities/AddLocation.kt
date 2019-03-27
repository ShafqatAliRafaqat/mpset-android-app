package com.mpset.pokerevents.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.mpset.pokerevents.R
import android.widget.Toast
import com.mpset.pokerevents.MainActivity
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment


class AddLocation : FragmentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        val autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                locationAddress = ""
                location_lat = 0.0
                location_lon = 0.0
//                val placeout = place.name.toString()
//                val lat = place.latLng.latitude
//                val lon = place.latLng.longitude
                locationAddress = place.name.toString()
                location_lat   = place.latLng.latitude
                location_lon   = place.latLng.longitude
//                Toast.makeText(this@AddLocation, placeout, Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@AddLocation, lat.toString(), Toast.LENGTH_SHORT).show()
//                Toast.makeText(this@AddLocation, lon.toString(), Toast.LENGTH_SHORT).show()
                finish()
                //                Log.i(TAG, "Place: " + place.getName());
            }

            override fun onError(status: Status) {

          Log.i("**", "An error occurred: " + status.statusCode + status.statusMessage)
            }
        })
        if(!locationAddress.equals("")){

        }else{
            Toast.makeText(applicationContext,"Please add your address",Toast.LENGTH_LONG).show()
        }
    }
    companion object {
        var locationAddress = ""
        var location_lat  = 0.0
        var location_lon = 0.0
//        var staticTest get() = vlaue
    }
}
