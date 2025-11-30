package com.cristiancogollo.digitalstreaming

import com.google.firebase.firestore.FirebaseFirestore

object DataSeeder {

    fun uploadInitialData(onResult: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()

        // ==========================================
        // 🖼️ LINKS DE IMÁGENES
        // ==========================================
        val imgNetflix = "https://drive.google.com/uc?export=view&id=11uBme6dppOCGQgEVBnx8OeKv0gad4GLe"
        val imgDisney = "https://drive.google.com/uc?export=view&id=1Ox8JvqbJaQ8DQuJM10KBfchhZy4ZmM6A"
        val imgPrime = "https://drive.google.com/uc?export=view&id=15x5yaEZcoMKA5EPAD7tolw2QAr_RA8qP"
        val imgHbo = "https://drive.google.com/uc?export=view&id=1v_hruDZnZGoT3Kv8CE1EYMSECrONPy-F"
        val imgParamount = "https://drive.google.com/uc?export=view&id=1zifm2N2UlMMNx0UVvhxmTV09dbIBvicO"
        val imgCrunchy = "https://drive.google.com/uc?export=view&id=1w30YZuWEXgMmojgbwjVLSzT9wA482wIg"
        val imgViki = "https://drive.google.com/uc?export=view&id=1uqugmyrhtuGHVU0zCy3b1S5Y7dzKgcYC"
        val imgVix = "https://drive.google.com/uc?export=view&id=1lhCa1wiRCCzs8Pql68qCzfJscaOpkHbm"
        val imgIptv = "https://drive.google.com/uc?export=view&id=19dIEK93_tr3cuU25DE_jsR4uw-8QcFme"

        val imgCanva = "https://drive.google.com/uc?export=view&id=14ZlAh1ulKLuEjJnVWIg3fmoZHcyZyp7r"
        val imgGpt = "https://drive.google.com/uc?export=view&id=1Vh6d0r5SEYsMzyAgfB73QiPGm5dVVkPZ"
        val imgGemini = "https://drive.google.com/uc?export=view&id=1fTQ1nhJqYl3Ql2g5TGH08VBpd7g7IYlu"

        val imgSpotify = "https://drive.google.com/uc?export=view&id=1RosItqj-7r1og8oorHVnTt0Mxq3UpGcq"
        val imgYoutube = "https://drive.google.com/uc?export=view&id=1BmPIyJdx4zAcJKl5OmzxP9pYmy5cFSEr"
        val imgApple = "https://drive.google.com/uc?export=view&id=1v8if5t2NTtb4LuIIcmkZk6R1uY6MygSn"
        val imgWin = "https://drive.google.com/uc?export=view&id=1-ZCvmz6E-7mQRF73O29RJtaeYeTAkacD"
        val imgXbox = "https://drive.google.com/uc?export=view&id=1-ZCvmz6E-7mQRF73O29RJtaeYeTAkacD"

        val imgPlex = ""
        val imgAdult = ""

        // NOTA: Usamos "name =", "salePrice =", etc. para evitar el error de tipos
        val initialProducts = listOf(
            // ==========================================
            // 📌 PLANES INDIVIDUALES (VIDEO)
            // ==========================================
            ProductModel(name = "Netflix 1P", salePrice = 12000.0, providers = mapOf("Dismanet" to 8500.0, "Prolive" to 8900.0), imageUrl = imgNetflix),
            ProductModel(name = "Disney+ 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0, "Prolive" to 2900.0), imageUrl = imgDisney),
            ProductModel(name = "Disney+ Premium", salePrice = 10000.0, providers = mapOf("Dismanet" to 6000.0, "Prolive" to 4900.0), imageUrl = imgDisney),
            ProductModel(name = "Prime Video 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3400.0, "Prolive" to 3500.0), imageUrl = imgPrime),
            ProductModel(name = "HBO Max 1P", salePrice = 8000.0, providers = mapOf("Dismanet" to 3000.0, "Prolive" to 4500.0), imageUrl = imgHbo),
            ProductModel(name = "Crunchyroll 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0, "Prolive" to 3500.0), imageUrl = imgCrunchy),
            ProductModel(name = "Viki 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0), imageUrl = imgViki),
            ProductModel(name = "Paramount+ 1P", salePrice = 8000.0, providers = mapOf("Dismanet" to 3100.0, "Prolive" to 3500.0), imageUrl = imgParamount),
            ProductModel(name = "Vix+ 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3200.0), imageUrl = imgVix),
            ProductModel(name = "Plex 1P", salePrice = 7000.0, providers = mapOf("Dismanet" to 3400.0), imageUrl = imgPlex),
            ProductModel(name = "IPTV Smarters (2 Meses)", salePrice = 14000.0, providers = mapOf("Dismanet" to 9500.0), serviceDays = 60, imageUrl = imgIptv),

            // ==========================================
            // 🎵 MÚSICA & AUDIO
            // ==========================================
            ProductModel(name = "Spotify 1 Mes", salePrice = 10000.0, providers = mapOf("Dismanet" to 6900.0), imageUrl = imgSpotify),
            ProductModel(name = "Spotify 2 Meses", salePrice = 12000.0, providers = mapOf("Dismanet" to 7000.0), serviceDays = 60, imageUrl = imgSpotify),
            ProductModel(name = "Spotify 3 Meses", salePrice = 16000.0, providers = mapOf("Dismanet" to 10300.0), serviceDays = 90, imageUrl = imgSpotify),
            ProductModel(name = "YouTube Premium 3 Meses", salePrice = 14000.0, providers = mapOf("Dismanet" to 8900.0), serviceDays = 90, imageUrl = imgYoutube),
            ProductModel(name = "YouTube Premium 4 Meses", salePrice = 18000.0, providers = mapOf("Dismanet" to 11300.0), serviceDays = 120, imageUrl = imgYoutube),
            ProductModel(name = "Apple Music 3 Meses", salePrice = 17000.0, providers = mapOf("Dismanet" to 11900.0), serviceDays = 90, imageUrl = imgApple),

            // ==========================================
            // ⚽ DEPORTES & JUEGOS
            // ==========================================
            ProductModel(name = "Win Sports+ (1 Mes)", salePrice = 20000.0, providers = mapOf("Dismanet" to 14900.0), imageUrl = imgWin),
            ProductModel(name = "Xbox Game Pass", salePrice = 16000.0, providers = mapOf("Dismanet" to 10900.0), imageUrl = imgXbox),

            // ==========================================
            // 🤖 IA & DISEÑO
            // ==========================================
            ProductModel(name = "Canva Pro", salePrice = 8000.0, providers = mapOf("Dismanet" to 2800.0), serviceDays = 365, imageUrl = imgCanva),
            ProductModel(name = "ChatGPT Plus", salePrice = 18000.0, providers = mapOf("Dismanet" to 15000.0), imageUrl = imgGpt),
            ProductModel(name = "Gemini Advanced", salePrice = 18000.0, providers = mapOf("Dismanet" to 15000.0), imageUrl = imgGemini),

            // ==========================================
            // 🔞 ADULTOS
            // ==========================================
            ProductModel(name = "Pornhub Premium", salePrice = 18000.0, providers = mapOf("Dismanet" to 11900.0), imageUrl = imgAdult),

            // ==========================================
            // 🔥 COMBOS MÁS VENDIDOS
            // ==========================================
            ProductModel(name = "Combo 1: Dúo Estrella", salePrice = 16000.0, providers = mapOf("Dismanet" to 11900.0), imageUrl = imgNetflix),
            ProductModel(name = "Combo 2: Cine Total", salePrice = 13000.0, providers = mapOf("Dismanet" to 6100.0), imageUrl = imgHbo),
            ProductModel(name = "Combo 3: Familiar Premium", salePrice = 15000.0, providers = mapOf("Dismanet" to 11700.0), imageUrl = imgDisney),
            ProductModel(name = "Combo 4: Anime + Series", salePrice = 15000.0, providers = mapOf("Dismanet" to 11700.0), imageUrl = imgCrunchy),
            ProductModel(name = "Combo 5: Triple Mega Pack", salePrice = 21000.0, providers = mapOf("Dismanet" to 15100.0), imageUrl = imgNetflix),
            ProductModel(name = "Combo 6: Cine Premium Pro", salePrice = 22000.0, providers = mapOf("Dismanet" to 14600.0), imageUrl = imgNetflix),
            ProductModel(name = "Combo IA: Inteligencia Total", salePrice = 32000.0, providers = mapOf("Dismanet" to 30000.0), imageUrl = imgGpt)
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
            .addOnSuccessListener { onResult("¡Dataset COMPLETO cargado con éxito!") }
            .addOnFailureListener { e -> onResult("Error: ${e.message}") }
    }
}