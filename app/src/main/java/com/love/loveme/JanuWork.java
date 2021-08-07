package com.love.loveme;

import android.content.Context;
import android.util.Log;

import com.love.loveme.models.CountryRoot;
import com.love.loveme.models.User;
import com.love.loveme.retrofit.Const;

import java.util.ArrayList;
import java.util.List;

public class JanuWork {
    protected static final List<String> names = new ArrayList<>();


    public static void setNames() {
        names.add("Manav");
        names.add("Paul");
        names.add("Jhon");
        names.add("Raja");
        names.add("Raghav");
        names.add("Viham");
        names.add("Aadi");
        names.add("Aarav");
        names.add("Baljiwan");
        names.add("Chatresh");
        names.add("Babu");
        names.add("Darsh");
        names.add("Dhruv");
        names.add("Aaban");
        names.add("Aabid");
        names.add("Aarav");
        names.add("Liam");
        names.add("Noah");
        names.add("Viham");
        names.add("William");
        names.add("Elijah");
        names.add("James");
        names.add("Daniel");
        names.add("Jack");
        names.add("Joseph");
        names.add("Jayden");
        names.add("Ekansh");
        names.add("Gaurang");
        names.add("Gabriel");
        names.add("Thomas");
        names.add("Joshua");
        names.add("Hudson");
        names.add("Eli");
        names.add("Jeremiah");
        names.add("Jace");
        names.add("Kayden");
        names.add("Weston");
        names.add("Sawyer");
        names.add("Micah");
        names.add("Ayden");
        names.add("Nathaniel");
        names.add("Tyler");
        names.add("Gavin");
        names.add("Bentley");
        names.add("Beau");
        names.add("Max");
        names.add("Giovanni");
        names.add("Calvin");
        names.add("Justin");
        names.add("Elliott");
        names.add("Alex");
        names.add("Brody");
        names.add("Finn");
        names.add("Arthur");
        names.add("Victor");
        names.add("Joel");
        names.add("Abraham");
        names.add("Richard");
        names.add("Steven");
        names.add("Peter");
        names.add("Andres");
        names.add("Amari");
        names.add("Adonis");
        names.add("Omar");
        names.add("Simon");
        names.add("Ronan");
        names.add("Roshan");
        names.add("Vinod");
        names.add("Binod");
        names.add("Bhavesh");
        names.add("Jemin");
        names.add("Mulla");
        names.add("Biku");
        names.add("Balu");
        names.add("Vikrant");
        names.add("Jaydev");
        names.add("Jadav");
        names.add("Manoj");
        names.add("Raja");
        names.add("Chirag");
        names.add("Lalu");
        names.add("Paresh");
        names.add("Vijay");
        names.add("Paul");
        names.add("Soham");
        names.add("Ayyer");
        names.add("Mitesh");
        names.add("Rahul");
        names.add("Krunal");
        names.add("Lax");
        names.add("Dax");
        names.add("Abhishek");
        names.add("Jems");

    }

    public static List<Integer> setGirlsPhotos1() {
        List<Integer> girlsPhotos = new ArrayList<>();
        girlsPhotos.add(R.drawable.mgirl1);
        girlsPhotos.add(R.drawable.mgirl2);
        girlsPhotos.add(R.drawable.mgirl3);
        girlsPhotos.add(R.drawable.mgirl4);
        girlsPhotos.add(R.drawable.mgirl5);
        girlsPhotos.add(R.drawable.mgirl6);
        girlsPhotos.add(R.drawable.mgirl6);
        girlsPhotos.add(R.drawable.mgirl8);
        return girlsPhotos;
    }

    public static List<Integer> setGirlsPhotos2() {
        List<Integer> girlsPhotos = new ArrayList<>();
        girlsPhotos.add(R.drawable.mgirl9);
        girlsPhotos.add(R.drawable.mgirl10);
        girlsPhotos.add(R.drawable.mgirl11);
        girlsPhotos.add(R.drawable.mgirl12);
        girlsPhotos.add(R.drawable.mgirl13);
        girlsPhotos.add(R.drawable.mgirl14);
        girlsPhotos.add(R.drawable.mgirl15);
        girlsPhotos.add(R.drawable.mgirl16);


        return girlsPhotos;
    }

    public static List<Integer> setGirlsPhotos3() {
        List<Integer> girlsPhotos = new ArrayList<>();
        girlsPhotos.add(R.drawable.mgirl17);
        girlsPhotos.add(R.drawable.mgirl18);
        girlsPhotos.add(R.drawable.mgirl19);
        girlsPhotos.add(R.drawable.mgirl20);
        girlsPhotos.add(R.drawable.mgirl21);
        girlsPhotos.add(R.drawable.mgirl22);
        girlsPhotos.add(R.drawable.mgirl23);
        girlsPhotos.add(R.drawable.mgirl24);

        return girlsPhotos;
    }

    public static CountryRoot.DataItem getCountry(String countryId, Context context) {
        SessionManager sessionManager = new SessionManager(context);
        List<CountryRoot.DataItem> countries = sessionManager.getCountry();
        if(countries != null) {
            for(CountryRoot.DataItem country : countries) {
                if(country.getCountryId().equals(countryId)) {
                    Log.d("TAG", "getCountry: sesson " + country.getCountry());
                    return country;
                }
            }
        }
        return null;
    }


    public boolean lessCoinLocal(Context context, int i) {
        SessionManager sessionManager = new SessionManager(context);
        if(sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            User user = sessionManager.getUser();
            int coin = Integer.parseInt(user.getMyWallet());
            if(coin >= i) {
                coin = coin - i;
                user.setMyWallet(String.valueOf(coin));
                sessionManager.saveUser(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean addCoinLocal(Context context, int i) {
        SessionManager sessionManager = new SessionManager(context);
        if(sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            User user = sessionManager.getUser();
            int coin = Integer.parseInt(user.getMyWallet());

            coin = coin + i;
            user.setMyWallet(String.valueOf(coin));
            sessionManager.saveUser(user);
            return true;

        } else {
            return false;
        }
    }


}
