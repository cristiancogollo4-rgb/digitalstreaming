package com.cristiancogollo.digitalstreaming

import com.google.firebase.firestore.FirebaseFirestore

object DataSeeder {

    fun uploadInitialData(onResult: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()

        // --- LINKS DE IMÁGENES (Convertidos para descarga directa) ---

        // Ya existentes
        val imgNetflix = "https://drive.google.com/uc?export=view&id=11uBme6dppOCGQgEVBnx8OeKv0gad4GLe"
        val imgDisney = "https://drive.google.com/uc?export=view&id=1Ox8JvqbJaQ8DQuJM10KBfchhZy4ZmM6A"
        val imgPrime = "https://drive.google.com/uc?export=view&id=15x5yaEZcoMKA5EPAD7tolw2QAr_RA8qP"
        val imgHbo = "https://drive.google.com/uc?export=view&id=1v_hruDZnZGoT3Kv8CE1EYMSECrONPy-F"
        val imgParamount = "https://drive.google.com/uc?export=view&id=1zifm2N2UlMMNx0UVvhxmTV09dbIBvicO"
        val imgCrunchy = "https://drive.google.com/uc?export=view&id=1w30YZuWEXgMmojgbwjVLSzT9wA482wIg"
        val imgViki = "https://drive.google.com/uc?export=view&id=1uqugmyrhtuGHVU0zCy3b1S5Y7dzKgcYC"
        val imgCanva = "https://drive.google.com/uc?export=view&id=14ZlAh1ulKLuEjJnVWIg3fmoZHcyZyp7r"
        val imgGpt = "https://drive.google.com/uc?export=view&id=1Vh6d0r5SEYsMzyAgfB73QiPGm5dVVkPZ"
        val imgGemini = "https://drive.google.com/uc?export=view&id=1fTQ1nhJqYl3Ql2g5TGH08VBpd7g7IYlu"

        // --- NUEVOS LINKS AGREGADOS ---
        val imgVix = "https://drive.google.com/uc?export=view&id=1lhCa1wiRCCzs8Pql68qCzfJscaOpkHbm"
        // NOTA: WinSports y Xbox tenían el mismo link en tu mensaje. Usé el mismo para ambos.
        val imgWin = "https://drive.google.com/uc?export=view&id=1-ZCvmz6E-7mQRF73O29RJtaeYeTAkacD"
        //https://drive.google.com/file/d/1-ZCvmz6E-7mQRF73O29RJtaeYeTAkacD/view?usp=sharing
        val imgXbox = "https://drive.google.com/file/d/1ZUEMM8ZR7JK_kI-th4TqeXk-ksOZxSYk"
        val imgYoutube = "https://drive.google.com/uc?export=view&id=1BmPIyJdx4zAcJKl5OmzxP9pYmy5cFSEr"
        val imgSpotify = "https://drive.google.com/uc?export=view&id=1RosItqj-7r1og8oorHVnTt0Mxq3UpGcq"
        val imgApple = "https://drive.google.com/uc?export=view&id=1v8if5t2NTtb4LuIIcmkZk6R1uY6MygSn"
        val imgIptv = "https://drive.google.com/uc?export=view&id=19dIEK93_tr3cuU25DE_jsR4uw-8QcFme"

        // Faltantes (Sin foto aún)
        val imgPlex = ""
        val imgAdult = ""

        val initialProducts = listOf(
            // --- VIDEO Y STREAMING ---
            ProductModel(name = "Netflix 1P", salePrice = 12000.0, providers = mapOf("Prolive" to 8900.0, "Camilo" to 7500.0), imageUrl = imgNetflix),
            ProductModel(name = "Disney+ 1P", salePrice = 7000.0, providers = mapOf("Prolive" to 2900.0, "Dismanet" to 3200.0), imageUrl = imgDisney),
            ProductModel(name = "Disney+ Premium", salePrice = 10000.0, providers = mapOf("Prolive" to 4900.0, "Dismanet" to 6000.0), imageUrl = imgDisney),
            ProductModel(name = "Amazon Prime 1P", salePrice = 7000.0, providers = mapOf("Prolive" to 3500.0, "Dismanet" to 3400.0), imageUrl = imgPrime),
            ProductModel(name = "HBO Max 1P", salePrice = 8000.0, providers = mapOf("Prolive" to 4500.0, "Dismanet" to 3000.0), imageUrl = imgHbo),
            ProductModel(name = "Paramount+ 1P", salePrice = 8000.0, providers = mapOf("Prolive" to 3500.0), imageUrl = imgParamount),
            ProductModel(name = "Crunchyroll 1P", salePrice = 7000.0, providers = mapOf("Prolive" to 3500.0), imageUrl = imgCrunchy),
            ProductModel(name = "Viki 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0), imageUrl = imgViki),

            // Nuevos con foto
            ProductModel(name = "Vix 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0), imageUrl = imgVix),
            ProductModel(name = "IPTV Smarters", salePrice = 14000.0, providers = mapOf("Dismanet" to 9500.0), imageUrl = imgIptv),
            ProductModel(name = "Win Sports", salePrice = 20000.0, providers = mapOf("Dismanet" to 14900.0), imageUrl = imgWin),
            ProductModel(name = "Xbox Game Pass", salePrice = 16000.0, providers = mapOf("Dismanet" to 10900.0), imageUrl = imgXbox),

            // --- IA Y UTILIDADES ---
            ProductModel(name = "ChatGPT Plus", salePrice = 25000.0, providers = mapOf("Dismanet" to 15000.0), imageUrl = imgGpt),
            ProductModel(name = "Gemini Advanced", salePrice = 25000.0, providers = mapOf("Dismanet" to 15000.0), imageUrl = imgGemini),
            ProductModel(name = "Canva (1 año)", salePrice = 8000.0, providers = mapOf("Dismanet" to 2800.0, "Prolive" to 0.0), serviceDays = 365, imageUrl = imgCanva),

            // --- SIN IMAGEN ---
            ProductModel(name = "Plex 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3400.0), imageUrl = imgPlex),

            // --- MÚSICA (Nuevos con Dismanet y Prolive en 0) ---

            // Spotify
            ProductModel(name = "Spotify 1 mes", salePrice = 10000.0, providers = mapOf("Dismanet" to 6900.0, "Prolive" to 0.0), serviceDays = 30, imageUrl = imgSpotify),
            ProductModel(name = "Spotify 2 meses", salePrice = 12000.0, providers = mapOf("Dismanet" to 7000.0, "Prolive" to 0.0), serviceDays = 60, imageUrl = imgSpotify),
            ProductModel(name = "Spotify 3 meses", salePrice = 16000.0, providers = mapOf("Dismanet" to 10300.0, "Prolive" to 0.0), serviceDays = 90, imageUrl = imgSpotify),

            // YouTube
            ProductModel(name = "YouTube Premium 3 meses", salePrice = 14000.0, providers = mapOf("Dismanet" to 8900.0, "Prolive" to 0.0), serviceDays = 90, imageUrl = imgYoutube),
            ProductModel(name = "YouTube Premium 4 meses", salePrice = 18000.0, providers = mapOf("Dismanet" to 11300.0, "Prolive" to 0.0), serviceDays = 120, imageUrl = imgYoutube),

            // Apple Music
            ProductModel(name = "Apple Music 3 meses", salePrice = 17000.0, providers = mapOf("Dismanet" to 11900.0, "Prolive" to 0.0), serviceDays = 90, imageUrl = imgApple),

            // Adultos
            ProductModel(name = "Pornhub Premium", salePrice = 18000.0, providers = mapOf("Dismanet" to 11900.0, "Prolive" to 0.0), serviceDays = 30, imageUrl = imgAdult)
        )

        initialProducts.forEach { product ->
            val docRef = db.collection("products").document()
            val data = hashMapOf(
                "id" to docRef.id,
                "name" to product.name,
                "salePrice" to product.salePrice,
                "providers" to product.providers,
                "serviceDays" to product.serviceDays,
                "imageUrl" to product.imageUrl
            )
            batch.set(docRef, data)
        }

        batch.commit()
            .addOnSuccessListener { onResult("¡Base de datos actualizada con nuevas FOTOS!") }
            .addOnFailureListener { e -> onResult("Error: ${e.message}") }
    }
}