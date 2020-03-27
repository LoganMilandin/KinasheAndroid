package com.kinashe.kinasheandroid.Utils;

import com.kinashe.kinasheandroid.ImageCard;
import com.kinashe.kinasheandroid.R;

import java.util.ArrayList;
import java.util.List;

public class ImageCardHelper {

    private List<ImageCard> cards;

    public ImageCardHelper(String screen) {
        /*accepts "places","entertainment","food","hair salon", "lodging", "attractions",
         * "medical", "shopping","other", "transportation", "air travel"*/
        cards = new ArrayList<>();
        if (screen == "places") {
            cards.add(new ImageCard("Bank | ባንክ", R.drawable.bank));
            cards.add(new ImageCard("Cafe | ካፌ", R.drawable.coffee));
            cards.add(new ImageCard("Entertainment | መዝናኛ", R.drawable.entertainment));
            cards.add(new ImageCard("Fitness and Health | የእስፓርት ማዕከሎች", R.drawable.fitness));
            cards.add(new ImageCard("Food | ምግብ", R.drawable.food));
            cards.add(new ImageCard("Hair Salon/Spa | የውበት ሳሎን እና ስፓ", R.drawable.spa));
            cards.add(new ImageCard("Lodging | የማረፍያ ቦታ", R.drawable.guest_house));
            cards.add(new ImageCard("Major Attractions | ዋና መስህብ", R.drawable.sightseeing));
            cards.add(new ImageCard("Medical | ሕክምና", R.drawable.hospital));
            cards.add(new ImageCard("Shopping | ግብይት", R.drawable.clothstore));
            cards.add(new ImageCard("Other | ቀሪ ነገሮች", R.drawable.hospital));
        } else if (screen == "entertainment") {
            cards.add(new ImageCard("Bar/Club | መጠጥ ቤትና ጭፈራ ቤት", R.drawable.entertainment));
            cards.add(new ImageCard("Cultural | ባህላዊ", R.drawable.dance));
            cards.add(new ImageCard("Movie Theater | የፊልም/ቲያትር ቤት", R.drawable.movie));
            cards.add(new ImageCard("Park/Recreation | ፓርክ-መዝናኛዎች", R.drawable.zoo));
        } else if (screen == "food") {
            cards.add(new ImageCard("Bakery | ዳቦ ቤት", R.drawable.bakery));
            cards.add(new ImageCard("Butcher Shop | ሥጋ ቤት", R.drawable.butcher));
            cards.add(new ImageCard("Grocery Store | ግሮሰሪ ሱቅ", R.drawable.grocery));
            cards.add(new ImageCard("Restaurant | ምግብ ቤት", R.drawable.food));
        } else if (screen == "hair salon") {
            cards.add(new ImageCard("Barber | ፀጉር አስተካካይ", R.drawable.barber));
            cards.add(new ImageCard("Salon | ሳሎን", R.drawable.salon));
            cards.add(new ImageCard("Spa | ስፓ", R.drawable.spa));
        } else if (screen == "lodging") {
            cards.add(new ImageCard("Guest House | የእንግዳ ማረፊያ", R.drawable.guest_house));
            cards.add(new ImageCard("Hotel | ሆቴል", R.drawable.hotel));
        } else if (screen == "attractions") {
            cards.add(new ImageCard("Monument | ሐውልት", R.drawable.monument));
            cards.add(new ImageCard("Museus | ሙዚየም", R.drawable.museum));
            cards.add(new ImageCard("Sightseeing |መጎብኘት", R.drawable.sightseeing));
        } else if (screen == "medical") {
            cards.add(new ImageCard("Hospital | ሆስፒታል", R.drawable.hospital));
            cards.add(new ImageCard("Pharmacy | መድኃኒት ቤት", R.drawable.pharmacy));
        } else if (screen == "shopping") {
            cards.add(new ImageCard("Bookstore | የመጻሕፍት መደብር", R.drawable.bookstore));
            cards.add(new ImageCard("Clothes | ልብስ", R.drawable.clothstore));
            cards.add(new ImageCard("Cosmetics | መዋቢያዎች", R.drawable.cosmetic_store));
            cards.add(new ImageCard("Electronics | ኤሌክትሮኒክስ", R.drawable.electronics_store));
            cards.add(new ImageCard("Footwear | ጫማ", R.drawable.footwear_store));
            cards.add(new ImageCard("Hardware | ሃርድዌር", R.drawable.hardware_store));
            cards.add(new ImageCard("Jewelry | ጌጣጌጥ", R.drawable.jewelry_store));
            cards.add(new ImageCard("Sporting Goods | የስፖርት ዕቃዎች", R.drawable.sporting_goods));
        } else if (screen == "other") {
            cards.add(new ImageCard("Church | ቤተክርስቲያን", R.drawable.church));
            cards.add(new ImageCard("School | ትምህርት ቤት", R.drawable.education1));
            cards.add(new ImageCard("Other |  ቀሪ ነገሮች", R.drawable.church));
        } else if (screen == "transportation") {
            cards.add(new ImageCard("Air Travel | የአየር ጉዞ", R.drawable.ethiopian_airlines));
            cards.add(new ImageCard("Car Rental | የመኪና ኪራይ", R.drawable.car_rental));
            cards.add(new ImageCard("Gas | ነዳ", R.drawable.gas));
            cards.add(new ImageCard("Taxi | ታክሲ", R.drawable.taxi));
            cards.add(new ImageCard("Train | ባቡር", R.drawable.train));
        } else if (screen == "air travel") {
            cards.add(new ImageCard("Airport | አየር ማረፊያ", R.drawable.ethiopian_airlines));
            cards.add(new ImageCard("Travel Agency |  የጉዞ ወኪል", R.drawable.travel_agent));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<ImageCard> get() {
        return cards;
    }
}
