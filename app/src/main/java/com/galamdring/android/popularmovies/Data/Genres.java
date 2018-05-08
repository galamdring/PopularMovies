package com.galamdring.android.popularmovies.Data;

import android.util.SparseArray;


public  class Genres {

    private static SparseArray<String> GenreMap = GetGenreArray();

    private static SparseArray<String> GetGenreArray() {
        SparseArray<String> array = new SparseArray<>();
        array.append(28,   "Action");
        array.append(12,   "Adventure");
        array.append(16,   "Animation");
        array.append(35,   "Comedy");
        array.append(80,   "Crime");
        array.append(99,   "Documentary");
        array.append(18,   "Drama");
        array.append(10751,"Family");
        array.append(14,   "Fantasy");
        array.append(36,   "History");
        array.append(27,   "Horror");
        array.append(10402,"Music");
        array.append(9648, "Mystery");
        array.append(10749,"Romance");
        array.append(878,  "Science Fiction");
        array.append(10770,"TV Movie");
        array.append(53,   "Thriller");
        array.append(10752,"War");
        array.append(37,   "Western");
        return array;
    }

    public static String GetGenre(int id){
        return GenreMap.get(id,"Unknown");
    }

    public static int GetGenre(String genre){
        int index = GenreMap.indexOfValue(genre);
        return GenreMap.keyAt(index);
    }
    /*
    "genres": [
    {
      "id": 28,"name": "Action"
    },{
      "id": 12,"name": "Adventure"
    },{
      "id": 16, "name": "Animation"
    },{
      "id": 35, "name": "Comedy"
    },{
      "id": 80, "name": "Crime"
    },    {
      "id": 99,      "name": "Documentary"
    },    {
      "id": 18,      "name": "Drama"
    },    {
      "id": 10751,      "name": "Family"
    },    {
      "id": 14,      "name": "Fantasy"
    },    {
      "id": 36,      "name": "History"
    },    {
      "id": 27,      "name": "Horror"
    },    {
      "id": 10402,      "name": "Music"
    },    {
      "id": 9648,      "name": "Mystery"
    },    {
      "id": 10749,      "name": "Romance"
    },    {
      "id": 878,      "name": "Science Fiction"
    },    {
      "id": 10770,      "name": "TV Movie"
    },    {
      "id": 53,      "name": "Thriller"
    },    {
      "id": 10752,      "name": "War"
    },    {
      "id": 37,      "name": "Western"
    }
  ]
     */
}
