package com.dolphinstudio.aicommerce.feature.product.data

import com.dolphinstudio.aicommerce.feature.product.domain.Product

object SampleCatalog {
    private val headphones = listOf(
        item("headphone_01", "WorkSound Pro", "Comfortable wireless noise cancelling headphones designed for long meetings and office work.", "Over-ear", 4200.0, "WorkSound", "black", 4.8, listOf("wireless", "meeting", "office", "comfortable", "noise cancelling"), "headphones_black"),
        item("headphone_02", "QuietLoop Air", "Light wireless headphones with soft cups for everyday calls and commute listening.", "Over-ear", 2800.0, "QuietLoop", "white", 4.4, listOf("wireless", "comfortable", "calls"), "headphones_white"),
        item("headphone_03", "Arena Blast", "Bold wireless gaming headphones with a tight seal and a long battery.", "Over-ear", 6100.0, "Arena", "black", 4.6, listOf("wireless", "gaming", "noise cancelling"), "headphones_gaming"),
        item("headphone_04", "Metro Buds", "Compact wireless earbuds that stay put on runs and short commutes.", "Earbuds", 1500.0, "Metro", "white", 4.2, listOf("wireless", "running", "lightweight"), "earbuds_white"),
        item("headphone_05", "Office Calm", "Blue wireless headphones tuned for long meetings and shared offices.", "Over-ear", 3900.0, "WorkSound", "blue", 4.7, listOf("wireless", "meetings", "comfortable", "office"), "headphones_blue"),
        item("headphone_06", "Studio Wire", "Wired studio headphones for focused desk work without a battery.", "Over-ear", 2200.0, "Harbor", "black", 4.1, listOf("wired", "meetings", "office"), "headphones_black"),
        item("headphone_07", "Night Run", "Sweat-ready wireless earbuds with a secure fit for evening runs.", "Earbuds", 3100.0, "NovaStep", "black", 4.5, listOf("wireless", "running", "secure fit"), "earbuds_black"),
        item("headphone_08", "Dollar Deck", "Simple wireless headphones for calls, sold in US dollars.", "On-ear", 80.0, "Harbor", "white", 4.0, listOf("wireless", "comfortable", "calls"), "headphones_white", "USD"),
        item("headphone_09", "Desk Mute", "Closed black headphones that keep office chatter out of long calls.", "Over-ear", 3400.0, "WorkSound", "black", 4.5, listOf("wireless", "meetings", "office", "comfortable"), "headphones_black"),
        item("headphone_10", "Cloud Cup", "Plush white headphones for comfortable all-day listening.", "Over-ear", 4600.0, "QuietLoop", "white", 4.6, listOf("wireless", "comfortable", "noise cancelling"), "headphones_white"),
        item("headphone_11", "Blue Note", "Blue wireless headphones with a light clamp for meetings.", "Over-ear", 3700.0, "Harbor", "blue", 4.3, listOf("wireless", "meetings", "comfortable"), "headphones_blue"),
        item("headphone_12", "Sprint Buds", "Tiny black earbuds built for running and a tight pocket.", "Earbuds", 1900.0, "NovaStep", "black", 4.4, listOf("wireless", "running", "lightweight"), "earbuds_black"),
        item("headphone_13", "Day Buds", "White earbuds for calls, walks, and a quiet commute.", "Earbuds", 1700.0, "Metro", "white", 4.1, listOf("wireless", "walking", "calls", "comfortable"), "earbuds_white"),
        item("headphone_14", "Raid Frame", "Firm gaming headphones with a deep seal and a long cable-free charge.", "Over-ear", 7200.0, "Arena", "black", 4.7, listOf("wireless", "gaming"), "headphones_gaming"),
        item("headphone_15", "Focus Pair", "Black meeting headphones that stay comfortable past the second hour.", "Over-ear", 5100.0, "WorkSound", "black", 4.9, listOf("wireless", "meetings", "comfortable", "noise cancelling"), "headphones_black"),
        item("headphone_16", "Pale Link", "White on-ear headphones for light calls and desk music.", "On-ear", 2300.0, "QuietLoop", "white", 4.0, listOf("wireless", "comfortable", "calls"), "headphones_white"),
        item("headphone_17", "Harbor Blue", "Blue everyday headphones with a soft headband.", "Over-ear", 2600.0, "Harbor", "blue", 4.2, listOf("wireless", "comfortable"), "headphones_blue"),
        item("headphone_18", "Track Pods", "Black sport earbuds with a wing tip for running.", "Earbuds", 2400.0, "NovaStep", "black", 4.3, listOf("wireless", "running"), "earbuds_black"),
        item("headphone_19", "Lobby Pair", "White headphones meant for open offices and short meetings.", "Over-ear", 3000.0, "WorkSound", "white", 4.4, listOf("wireless", "meetings", "office"), "headphones_white"),
        item("headphone_20", "Arena Mini", "Smaller black gaming headset for late desk sessions.", "Over-ear", 5400.0, "Arena", "black", 4.2, listOf("wireless", "gaming", "desk"), "headphones_gaming"),
    )

    private val shoes = listOf(
        item("shoe_01", "NovaStep Run", "Cushioned running shoes with a breathable upper for daily mileage.", "Running", 2400.0, "NovaStep", "black", 4.7, listOf("running", "cushioned", "breathable"), "shoes_black_run"),
        item("shoe_02", "TrailLine Walk", "Soft walking shoes made for long city routes and weekend errands.", "Walking", 1800.0, "TrailLine", "white", 4.3, listOf("walking", "comfortable", "city"), "shoes_white_walk"),
        item("shoe_03", "Harbor Sprint", "Light racing-day trainers with a responsive sole.", "Running", 3200.0, "Harbor", "blue", 4.6, listOf("running", "lightweight", "responsive"), "shoes_blue_run"),
        item("shoe_04", "City Mover", "Comfortable black walking shoes for commutes and standing desks.", "Walking", 2100.0, "City", "black", 4.4, listOf("walking", "comfortable", "commute"), "shoes_black_run"),
        item("shoe_05", "Court Ace", "Clean court shoes with a stable base for casual sport.", "Court", 4500.0, "Atlas", "white", 4.5, listOf("court", "stable", "casual"), "shoes_white_court"),
        item("shoe_06", "Sand Walk", "Easy walking sandals with a cushioned footbed.", "Walking", 900.0, "TrailLine", "white", 4.0, listOf("walking", "comfortable", "sandals"), "shoes_sandals"),
        item("shoe_07", "Night Mile", "Dark running shoes with reflective hits for evening training.", "Running", 2900.0, "NovaStep", "black", 4.6, listOf("running", "reflective", "evening"), "shoes_black_run"),
        item("shoe_08", "Budget Flex", "Flexible everyday shoes for walking without a high price.", "Walking", 1200.0, "City", "blue", 3.9, listOf("walking", "flexible", "everyday"), "shoes_blue_run"),
        item("shoe_09", "Mile Cushion", "Daily black trainers with extra foam for easy runs.", "Running", 2700.0, "NovaStep", "black", 4.5, listOf("running", "comfortable", "cushioned"), "shoes_black_run"),
        item("shoe_10", "Sunday Walk", "White walking shoes with a soft collar and a quiet sole.", "Walking", 1600.0, "TrailLine", "white", 4.4, listOf("walking", "comfortable"), "shoes_white_walk"),
        item("shoe_11", "Blue Tempo", "Blue lightweight running shoes for shorter speed days.", "Running", 3500.0, "Harbor", "blue", 4.7, listOf("running", "lightweight"), "shoes_blue_run"),
        item("shoe_12", "Court Clean", "Minimal white court shoes for casual days.", "Court", 3800.0, "Atlas", "white", 4.3, listOf("court", "casual", "comfortable"), "shoes_white_court"),
        item("shoe_13", "Shore Slide", "Simple sandals for slow walks and warm afternoons.", "Walking", 750.0, "TrailLine", "white", 3.8, listOf("walking", "sandals", "comfortable"), "shoes_sandals"),
        item("shoe_14", "Commute Black", "Black walking shoes that stay comfortable on tiled floors.", "Walking", 2300.0, "City", "black", 4.2, listOf("walking", "comfortable", "commute"), "shoes_black_run"),
        item("shoe_15", "River Run", "Blue trainers with a grippy outsole for mixed paths.", "Running", 3100.0, "Harbor", "blue", 4.4, listOf("running", "grip"), "shoes_blue_run"),
        item("shoe_16", "Pale Court", "White low court shoes with a stable heel.", "Court", 4100.0, "Atlas", "white", 4.6, listOf("court", "stable"), "shoes_white_court"),
        item("shoe_17", "Easy Mile", "Approachable black running shoes for a first 5k.", "Running", 1900.0, "NovaStep", "black", 4.1, listOf("running", "comfortable"), "shoes_black_run"),
        item("shoe_18", "Market Walk", "White city walking shoes for errands and long afternoons.", "Walking", 1400.0, "City", "white", 4.0, listOf("walking", "city", "comfortable"), "shoes_white_walk"),
        item("shoe_19", "Desk Stand", "Black comfort shoes for people who stand through meetings.", "Walking", 2500.0, "TrailLine", "black", 4.5, listOf("walking", "comfortable", "meetings"), "shoes_black_run"),
        item("shoe_20", "Dollar Stride", "Light walking shoes priced in US dollars.", "Walking", 45.0, "Harbor", "white", 4.0, listOf("walking", "lightweight", "comfortable"), "shoes_white_walk", "USD"),
    )

    private val laptops = listOf(
        item("laptop_01", "PaperNote Air", "Lightweight laptop for programming, notes, and long writing sessions.", "Ultrabook", 28000.0, "PaperNote", "white", 4.8, listOf("lightweight", "programming", "writing"), "laptop_silver"),
        item("laptop_02", "Northwind Dev", "Developer laptop with a bright keyboard and room for local projects.", "Developer", 36000.0, "Northwind", "black", 4.7, listOf("programming", "keyboard", "developer"), "laptop_black"),
        item("laptop_03", "Lumen Game", "High-refresh gaming laptop for evening sessions at a desk.", "Gaming", 42000.0, "Lumen", "black", 4.5, listOf("gaming", "performance", "desk"), "laptop_gaming"),
        item("laptop_04", "Campus Lite", "Lightweight student laptop for programming classes and essays.", "Ultrabook", 19000.0, "PaperNote", "blue", 4.3, listOf("lightweight", "programming", "student"), "laptop_blue"),
        item("laptop_05", "Atlas Pro", "Powerful work laptop for compiling, design files, and travel.", "Pro", 45000.0, "Atlas", "black", 4.6, listOf("programming", "powerful", "travel"), "laptop_black"),
        item("laptop_06", "Travel Sheet", "Thin laptop that stays light in a commuter bag.", "Ultrabook", 24000.0, "Harbor", "white", 4.4, listOf("lightweight", "travel", "commute"), "laptop_silver"),
        item("laptop_07", "Bluebook 15", "Blue everyday laptop for programming and browsing.", "Everyday", 31000.0, "Northwind", "blue", 4.2, listOf("programming", "everyday", "browser"), "laptop_blue"),
        item("laptop_08", "Dollar Book", "Compact programming laptop priced in US dollars.", "Ultrabook", 900.0, "PaperNote", "white", 4.1, listOf("lightweight", "programming"), "laptop_silver", "USD"),
        item("laptop_09", "Quiet Compile", "Black developer laptop that stays cool during long builds.", "Developer", 39000.0, "Northwind", "black", 4.8, listOf("programming", "developer"), "laptop_black"),
        item("laptop_10", "Page Air", "Very light silver laptop for writing and programming notes.", "Ultrabook", 26500.0, "PaperNote", "white", 4.6, listOf("lightweight", "programming", "writing"), "laptop_silver"),
        item("laptop_11", "Night Raid", "Black gaming laptop with a firm keyboard for desk play.", "Gaming", 48000.0, "Lumen", "black", 4.4, listOf("gaming", "desk"), "laptop_gaming"),
        item("laptop_12", "Study Blue", "Blue student laptop for programming labs.", "Everyday", 21000.0, "Harbor", "blue", 4.1, listOf("programming", "lightweight", "student"), "laptop_blue"),
        item("laptop_13", "Field Pro", "Black travel laptop for code reviews on the move.", "Pro", 41000.0, "Atlas", "black", 4.5, listOf("programming", "travel", "lightweight"), "laptop_black"),
        item("laptop_14", "Sheet 13", "Small silver laptop that disappears in a tote.", "Ultrabook", 23000.0, "PaperNote", "white", 4.3, listOf("lightweight", "commute"), "laptop_silver"),
        item("laptop_15", "Arena Deck", "Gaming laptop for evening sessions without leaving the desk.", "Gaming", 52000.0, "Arena", "black", 4.6, listOf("gaming", "performance"), "laptop_gaming"),
        item("laptop_16", "Blue Draft", "Blue laptop for programming, slides, and browsing.", "Everyday", 27500.0, "Northwind", "blue", 4.0, listOf("programming", "everyday"), "laptop_blue"),
        item("laptop_17", "Kernel Book", "Black programming laptop with a deep keyboard.", "Developer", 44000.0, "Northwind", "black", 4.9, listOf("programming", "keyboard", "developer"), "laptop_black"),
        item("laptop_18", "Carry Light", "Silver lightweight laptop for meetings and writing.", "Ultrabook", 30000.0, "Harbor", "white", 4.5, listOf("lightweight", "meetings", "writing"), "laptop_silver"),
        item("laptop_19", "Match Night", "Black gaming laptop with a quiet fan at idle.", "Gaming", 39000.0, "Lumen", "black", 4.2, listOf("gaming", "quiet"), "laptop_gaming"),
        item("laptop_20", "Campus Blue", "Affordable blue laptop for programming coursework.", "Everyday", 17500.0, "PaperNote", "blue", 3.9, listOf("programming", "student", "lightweight"), "laptop_blue"),
    )

    private val fashion = listOf(
        item("fashion_01", "CairoThread Shirt", "Breathable white shirt for warm days and easy outfits.", "Shirts", 850.0, "CairoThread", "white", 4.4, listOf("comfortable", "breathable", "shirt"), "shirt_white"),
        item("fashion_02", "Navy Day Dress", "Simple blue day dress with a clean line.", "Dresses", 1400.0, "CairoThread", "blue", 4.5, listOf("dress", "day", "simple"), "dress_blue"),
        item("fashion_03", "Office Knit", "Soft black knit made for meetings and air-conditioned offices.", "Knitwear", 1100.0, "Northwind", "black", 4.6, listOf("meetings", "comfortable", "office"), "knit_black"),
        item("fashion_04", "Weekend Linen", "Loose white linen for slow weekends.", "Shirts", 1600.0, "CairoThread", "white", 4.3, listOf("comfortable", "linen", "weekend"), "shirt_white"),
        item("fashion_05", "Meeting Blazer", "Structured black blazer for meetings and presentations.", "Tailoring", 2500.0, "Atlas", "black", 4.7, listOf("meetings", "tailored", "presentation"), "blazer_black"),
        item("fashion_06", "Walk Coat", "Light blue coat for walking between meetings.", "Outerwear", 2100.0, "Harbor", "blue", 4.2, listOf("walking", "coat", "light"), "coat_blue"),
        item("fashion_07", "Soft Tee", "Everyday white tee with a soft hand feel.", "T-shirts", 450.0, "CairoThread", "white", 4.1, listOf("comfortable", "everyday", "tee"), "tee_white"),
        item("fashion_08", "Night Jacket", "Black jacket with a quiet finish for evenings out.", "Outerwear", 1900.0, "Atlas", "black", 4.4, listOf("jacket", "evening"), "jacket_black"),
        item("fashion_09", "Desk Shirt", "Crisp white shirt that stays comfortable in long meetings.", "Shirts", 980.0, "CairoThread", "white", 4.5, listOf("meetings", "comfortable", "shirt"), "shirt_white"),
        item("fashion_10", "Blue Day", "Easy blue dress for warm afternoons.", "Dresses", 1550.0, "Harbor", "blue", 4.2, listOf("dress", "comfortable"), "dress_blue"),
        item("fashion_11", "Quiet Knit", "Black knit with a soft neck for office air conditioning.", "Knitwear", 1300.0, "Northwind", "black", 4.4, listOf("comfortable", "office", "meetings"), "knit_black"),
        item("fashion_12", "Pitch Blazer", "Black blazer with a cleaner shoulder for presentations.", "Tailoring", 2800.0, "Atlas", "black", 4.6, listOf("meetings", "tailored"), "blazer_black"),
        item("fashion_13", "Route Coat", "Blue coat light enough for walking across town.", "Outerwear", 2400.0, "Harbor", "blue", 4.3, listOf("walking", "coat", "comfortable"), "coat_blue"),
        item("fashion_14", "Plain Tee", "White tee with a relaxed comfortable fit.", "T-shirts", 390.0, "CairoThread", "white", 4.0, listOf("comfortable", "everyday", "tee"), "tee_white"),
        item("fashion_15", "After Jacket", "Black jacket for evenings after work.", "Outerwear", 2200.0, "Atlas", "black", 4.5, listOf("jacket", "evening"), "jacket_black"),
        item("fashion_16", "Linen Hour", "White shirt in a looser comfortable cut.", "Shirts", 1200.0, "CairoThread", "white", 4.2, listOf("comfortable", "linen"), "shirt_white"),
        item("fashion_17", "Meeting Knit", "Black knit you can wear straight into a meeting.", "Knitwear", 1450.0, "Northwind", "black", 4.7, listOf("meetings", "comfortable"), "knit_black"),
        item("fashion_18", "Soft Blazer", "Black blazer with a less stiff comfortable lining.", "Tailoring", 2300.0, "Atlas", "black", 4.3, listOf("meetings", "comfortable", "tailored"), "blazer_black"),
        item("fashion_19", "City Coat", "Blue everyday coat for walking and cool rooms.", "Outerwear", 1950.0, "Harbor", "blue", 4.1, listOf("walking", "coat"), "coat_blue"),
        item("fashion_20", "Dollar Tee", "Soft white tee priced in US dollars.", "T-shirts", 18.0, "CairoThread", "white", 4.0, listOf("comfortable", "tee"), "tee_white", "USD"),
    )

    private val accessories = listOf(
        item("acc_01", "Desk Sleeve", "Padded black sleeve that keeps a laptop quiet in a bag.", "Laptop", 600.0, "Harbor", "black", 4.5, listOf("laptop", "padded", "commute"), "sleeve_black"),
        item("acc_02", "Meet Mouse", "Compact wireless mouse for meetings and small desks.", "Desk", 450.0, "WorkSound", "black", 4.3, listOf("wireless", "meetings", "desk"), "mouse_black"),
        item("acc_03", "Run Belt", "Slim belt that holds a phone steady on runs.", "Sport", 350.0, "NovaStep", "black", 4.4, listOf("running", "phone", "slim"), "belt_black"),
        item("acc_04", "Blue Strap", "Woven blue strap for a bag or camera.", "Bags", 250.0, "Harbor", "blue", 4.0, listOf("strap", "bag"), "strap_blue"),
        item("acc_05", "Comfort Cushion", "Seat cushion for long desk sessions and calls.", "Desk", 700.0, "WorkSound", "white", 4.6, listOf("comfortable", "desk", "meetings"), "cushion_white"),
        item("acc_06", "White Tote", "Open white tote for a laptop and a notebook.", "Bags", 500.0, "CairoThread", "white", 4.2, listOf("tote", "laptop", "everyday"), "tote_white"),
        item("acc_07", "Cable Kit", "Short cable kit for a tidy desk.", "Desk", 200.0, "Harbor", "black", 3.8, listOf("cables", "desk"), "cables_black"),
        item("acc_08", "Dollar Case", "Hard phone case priced in US dollars.", "Phone", 25.0, "Atlas", "blue", 4.0, listOf("phone", "case"), "case_blue", "USD"),
        item("acc_09", "Commute Sleeve", "Slim black laptop sleeve for a daily bag.", "Laptop", 480.0, "Harbor", "black", 4.3, listOf("laptop", "commute", "lightweight"), "sleeve_black"),
        item("acc_10", "Quiet Click", "Small black wireless mouse for meeting rooms.", "Desk", 520.0, "WorkSound", "black", 4.5, listOf("wireless", "meetings", "comfortable"), "mouse_black"),
        item("acc_11", "Mile Belt", "Black running belt with a flat phone pocket.", "Sport", 420.0, "NovaStep", "black", 4.6, listOf("running", "phone"), "belt_black"),
        item("acc_12", "Camera Strap", "Blue woven strap that sits soft on the shoulder.", "Bags", 320.0, "Harbor", "blue", 4.1, listOf("strap", "comfortable"), "strap_blue"),
        item("acc_13", "Long Call Cushion", "White cushion for chairs during long meetings.", "Desk", 640.0, "WorkSound", "white", 4.4, listOf("comfortable", "meetings", "desk"), "cushion_white"),
        item("acc_14", "Day Tote", "White tote with room for a light laptop.", "Bags", 560.0, "CairoThread", "white", 4.3, listOf("tote", "laptop", "lightweight"), "tote_white"),
        item("acc_15", "Short Cable", "Black cable set that keeps a desk clear.", "Desk", 180.0, "Harbor", "black", 3.9, listOf("cables", "desk"), "cables_black"),
        item("acc_16", "Blue Shell", "Matte blue phone case with a firm rim.", "Phone", 300.0, "Atlas", "blue", 4.2, listOf("phone", "case"), "case_blue"),
        item("acc_17", "Travel Sleeve", "Black sleeve sized for a lightweight laptop.", "Laptop", 720.0, "Atlas", "black", 4.7, listOf("laptop", "travel", "lightweight"), "sleeve_black"),
        item("acc_18", "Desk Mouse", "Black wireless mouse with a quiet click.", "Desk", 390.0, "Northwind", "black", 4.1, listOf("wireless", "desk", "programming"), "mouse_black"),
        item("acc_19", "Evening Tote", "White tote for a notebook and a light jacket.", "Bags", 610.0, "CairoThread", "white", 4.0, listOf("tote", "everyday"), "tote_white"),
        item("acc_20", "Soft Seat", "White desk cushion for comfortable long sessions.", "Desk", 760.0, "WorkSound", "white", 4.8, listOf("comfortable", "desk", "office"), "cushion_white"),
    )

    private fun item(
        id: String,
        name: String,
        description: String,
        subcategory: String,
        price: Double,
        brand: String,
        color: String,
        rating: Double,
        tags: List<String>,
        image: String,
        currency: String = "EGP",
    ): Product {
        val category = when {
            id.startsWith("headphone") -> "Headphones"
            id.startsWith("shoe") -> "Shoes"
            id.startsWith("laptop") -> "Laptops"
            id.startsWith("fashion") -> "Fashion"
            else -> "Accessories"
        }
        return Product(
            id = id,
            name = name,
            description = description,
            category = category,
            subcategory = subcategory,
            price = price,
            currency = currency,
            brand = brand,
            color = color,
            rating = rating,
            imageUrl = image,
            tags = tags,
        )
    }

    val products: List<Product> = headphones + shoes + laptops + fashion + accessories

    val brands: List<String> = products.mapNotNull { it.brand }.distinct()
}
