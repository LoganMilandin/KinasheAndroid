package com.kinashe.kinasheandroid.Utils;

import android.util.Log;

import com.kinashe.kinasheandroid.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the only thing interesting about this class is that it contains a map
 * of strings to subtypes of those strings in order to define the navigation
 * on the places and transportation pages
 */
public class ImageCardHelper {

    private static final String TAG = "ImageCardHelper";

    private Map<String, ImageCard[]> categoryMap;
    private String currScreen;

    public ImageCardHelper() {
        categoryMap = new HashMap<>();
        categoryMap.put("Places | ቦታዎች", new ImageCard[]{
                new ImageCard("Bank | ባንክ", R.drawable.bank),
                new ImageCard("Cafe | ካፌ", R.drawable.coffee),
                new ImageCard("Entertainment | መዝናኛ", R.drawable.entertainment),
                new ImageCard("Fitness and Health | የእስፓርት ማዕከሎች", R.drawable.fitness),
                new ImageCard("Food | ምግብ", R.drawable.food),
                new ImageCard("Hair Salon/Spa | የውበት ሳሎን እና ስፓ", R.drawable.spa),
                new ImageCard("Lodging | የማረፍያ ቦታ", R.drawable.guest_house),
                new ImageCard("Major Attractions | ዋና መስህብ", R.drawable.sightseeing),
                new ImageCard("Medical | ሕክምና", R.drawable.hospital),
                new ImageCard("Shopping | ግብይት", R.drawable.clothstore),
                new ImageCard("Other | ቀሪ ነገሮች", R.drawable.other_button)
        });
        Log.d(TAG, "mapping");
        categoryMap.put("Entertainment | መዝናኛ", new ImageCard[]{
                new ImageCard("Bar/Club | መጠጥ ቤትና ጭፈራ ቤት", R.drawable.entertainment),
                new ImageCard("Cultural | ባህላዊ", R.drawable.dance),
                new ImageCard("Movie Theater | የፊልም/ቲያትር ቤት", R.drawable.movie),
                new ImageCard("Park/Recreation | ፓርክ-መዝናኛዎች", R.drawable.zoo)
        });
        categoryMap.put("Food | ምግብ", new ImageCard[]{
                new ImageCard("Bakery | ዳቦ ቤት", R.drawable.bakery),
                new ImageCard("Butcher Shop | ሥጋ ቤት", R.drawable.butcher),
                new ImageCard("Grocery Store | ግሮሰሪ ሱቅ", R.drawable.grocery),
                new ImageCard("Restaurant | ምግብ ቤት", R.drawable.food)
        });
        categoryMap.put("Hair Salon/Spa | የውበት ሳሎን እና ስፓ", new ImageCard[]{
                new ImageCard("Barber | ፀጉር አስተካካይ", R.drawable.barber),
                new ImageCard("Salon | ሳሎን", R.drawable.salon),
                new ImageCard("Spa | ስፓ", R.drawable.spa)
        });
        categoryMap.put("Lodging | የማረፍያ ቦታ", new ImageCard[]{
                new ImageCard("Guest House | የእንግዳ ማረፊያ", R.drawable.guest_house),
                new ImageCard("Hotel | ሆቴል", R.drawable.hotel)
        });

        categoryMap.put("Major Attractions | ዋና መስህብ", new ImageCard[]{
                new ImageCard("Monument | ሐውልት", R.drawable.monument),
                new ImageCard("Museum | ሙዚየም", R.drawable.museum),
                new ImageCard("Sightseeing |መጎብኘት", R.drawable.sightseeing),
        });
        categoryMap.put("Medical | ሕክምና", new ImageCard[]{
                new ImageCard("Hospital | ሆስፒታል", R.drawable.hospital),
                new ImageCard("Pharmacy | መድኃኒት ቤት", R.drawable.pharmacy)
        });
        categoryMap.put("Shopping | ግብይት", new ImageCard[]{
                new ImageCard("Bookstore | የመጻሕፍት መደብር", R.drawable.bookstore),
                new ImageCard("Clothes | ልብስ", R.drawable.clothstore),
                new ImageCard("Cosmetics | መዋቢያዎች", R.drawable.cosmetic_store),
                new ImageCard("Electronics | ኤሌክትሮኒክስ", R.drawable.electronics_store),
                new ImageCard("Footwear | ጫማ", R.drawable.footwear_store),
                new ImageCard("Hardware | ሃርድዌር", R.drawable.hardware_store),
                new ImageCard("Jewelry | ጌጣጌጥ", R.drawable.jewelry_store),
                new ImageCard("Sporting Goods | የስፖርት ዕቃዎች", R.drawable.sporting_goods)
        });
        categoryMap.put("Other | ቀሪ ነገሮች", new ImageCard[]{
                new ImageCard("Church | ቤተክርስቲያን", R.drawable.church),
                new ImageCard("School | ትምህርት ቤት", R.drawable.education1),
                new ImageCard("Other |  ቀሪ ነገሮች", R.drawable.other_button)
        });
        categoryMap.put("Transportation | መጓጓዣ", new ImageCard[]{
                new ImageCard("Air Travel | የአየር ጉዞ", R.drawable.ethiopian_airlines),
                new ImageCard("Car Rental | የመኪና ኪራይ", R.drawable.car_rental),
                new ImageCard("Gas | ነዳ", R.drawable.gas),
                new ImageCard("Taxi | ታክሲ", R.drawable.taxi),
                new ImageCard("Train | ባቡር", R.drawable.train)
        });
        categoryMap.put("Air Travel | የአየር ጉዞ", new ImageCard[]{
                new ImageCard("Airport | አየር ማረፊያ", R.drawable.ethiopian_airlines),
                new ImageCard("Travel Agency |  የጉዞ ወኪል", R.drawable.travel_agent)
        });
    }

    public List<ImageCard> getCards(String title) {
        if (categoryMap.get(title) != null) {
            return Arrays.asList(categoryMap.get(title));
        }
        return null;
    }
}
