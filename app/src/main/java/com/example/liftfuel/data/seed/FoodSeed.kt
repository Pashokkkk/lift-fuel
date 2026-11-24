package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.Food
import com.example.liftfuel.data.local.entity.FoodCategory
import com.example.liftfuel.data.local.entity.FoodCategory.DAIRY_EGGS
import com.example.liftfuel.data.local.entity.FoodCategory.DRINKS
import com.example.liftfuel.data.local.entity.FoodCategory.FATS_OILS
import com.example.liftfuel.data.local.entity.FoodCategory.FRUIT
import com.example.liftfuel.data.local.entity.FoodCategory.GRAINS_BREAD
import com.example.liftfuel.data.local.entity.FoodCategory.LEGUMES_NUTS
import com.example.liftfuel.data.local.entity.FoodCategory.MEAT_FISH
import com.example.liftfuel.data.local.entity.FoodCategory.READY_MEALS
import com.example.liftfuel.data.local.entity.FoodCategory.SAUCES
import com.example.liftfuel.data.local.entity.FoodCategory.SNACKS_SWEETS
import com.example.liftfuel.data.local.entity.FoodCategory.SUPPLEMENTS
import com.example.liftfuel.data.local.entity.FoodCategory.VEGETABLES
import com.example.liftfuel.data.local.entity.FoodSource
import java.time.Instant

/**
 * Built-in food table, seeded on first launch so search works offline.
 *
 * Values are approximate per 100 g, rounded from USDA FoodData Central and typical
 * EU/UK labels. Carbs are total carbohydrate including fibre. Good enough for a
 * beginner-friendly tracker; Open Food Facts results add exact branded products.
 *
 * Barcodes: the 14 demo barcodes use the 200–299 prefix, which GS1 reserves for
 * in-store use, so they never clash with real products scanned via Open Food Facts.
 */
object FoodSeed {

    fun foods(createdAt: Instant): List<Food> {
        fun food(
            name: String,
            category: FoodCategory,
            kcal: Number,
            protein: Number,
            carbs: Number,
            fat: Number,
            fiber: Number? = null,
            servingG: Number? = null,
            servingLabel: String? = null,
            barcode: String? = null,
        ) = Food(
            name = name,
            category = category,
            kcalPer100g = kcal.toDouble(),
            proteinPer100g = protein.toDouble(),
            carbsPer100g = carbs.toDouble(),
            fatPer100g = fat.toDouble(),
            fiberPer100g = fiber?.toDouble(),
            servingSizeG = servingG?.toDouble(),
            servingLabel = servingLabel,
            barcode = barcode,
            source = FoodSource.BUILT_IN,
            createdAt = createdAt,
        )

        return listOf(
            // ---- Meat & fish ----
            food("Chicken breast, cooked", MEAT_FISH, 165, 31.0, 0, 3.6, servingG = 150, servingLabel = "1 fillet"),
            food("Chicken breast, raw", MEAT_FISH, 120, 22.5, 0, 2.6, servingG = 170, servingLabel = "1 fillet"),
            food("Chicken thigh, cooked, skinless", MEAT_FISH, 209, 26.0, 0, 10.9, servingG = 100, servingLabel = "1 thigh"),
            food("Turkey breast, cooked", MEAT_FISH, 135, 30.0, 0, 1.0, servingG = 120),
            food("Beef mince 5% fat, cooked", MEAT_FISH, 170, 26.0, 0, 7.0, servingG = 125),
            food("Beef mince 20% fat, cooked", MEAT_FISH, 254, 25.0, 0, 17.0, servingG = 125),
            food("Sirloin steak, cooked", MEAT_FISH, 206, 29.0, 0, 9.7, servingG = 200, servingLabel = "1 steak"),
            food("Pork loin, cooked", MEAT_FISH, 197, 27.0, 0, 9.5, servingG = 150),
            food("Back bacon, grilled", MEAT_FISH, 287, 24.0, 0, 21.0, servingG = 25, servingLabel = "1 rasher"),
            food("Ham, sliced", MEAT_FISH, 110, 18.0, 1.5, 3.5, servingG = 30, servingLabel = "2 slices"),
            food("Pork sausage, grilled", MEAT_FISH, 294, 16.2, 9.8, 21.2, servingG = 45, servingLabel = "1 sausage"),
            food("Salmon fillet, baked", MEAT_FISH, 206, 22.1, 0, 12.4, servingG = 130, servingLabel = "1 fillet"),
            food("Smoked salmon", MEAT_FISH, 117, 18.3, 0, 4.3, servingG = 50),
            food("Tuna, canned in water, drained", MEAT_FISH, 116, 25.5, 0, 0.8, servingG = 112, servingLabel = "1 can", barcode = "2001234000079"),
            food("Cod fillet, baked", MEAT_FISH, 105, 22.8, 0, 0.9, servingG = 140, servingLabel = "1 fillet"),
            food("Prawns, cooked", MEAT_FISH, 99, 24.0, 0.2, 0.3, servingG = 100),
            food("Mackerel, cooked", MEAT_FISH, 262, 23.9, 0, 17.8, servingG = 100),
            food("Sardines in olive oil, drained", MEAT_FISH, 208, 24.6, 0, 11.5, servingG = 90, servingLabel = "1 can"),

            // ---- Dairy & eggs ----
            food("Egg, whole, boiled", DAIRY_EGGS, 155, 12.6, 1.1, 10.6, servingG = 50, servingLabel = "1 large egg"),
            food("Egg white", DAIRY_EGGS, 52, 10.9, 0.7, 0.2, servingG = 33, servingLabel = "1 egg white"),
            food("Omelette with cheese", DAIRY_EGGS, 190, 12.5, 1.0, 15.0, servingG = 150, servingLabel = "2-egg omelette"),
            food("Milk, semi-skimmed", DAIRY_EGGS, 47, 3.5, 4.8, 1.7, servingG = 250, servingLabel = "1 glass (250 ml)", barcode = "2001234000109"),
            food("Milk, whole", DAIRY_EGGS, 64, 3.3, 4.7, 3.6, servingG = 250, servingLabel = "1 glass (250 ml)"),
            food("Milk, skimmed", DAIRY_EGGS, 35, 3.4, 5.0, 0.1, servingG = 250, servingLabel = "1 glass (250 ml)"),
            food("Greek yogurt 0% fat", DAIRY_EGGS, 59, 10.3, 3.6, 0.4, servingG = 170, servingLabel = "1 pot", barcode = "2001234000017"),
            food("Greek yogurt, full fat", DAIRY_EGGS, 97, 9.0, 3.9, 5.0, servingG = 170, servingLabel = "1 pot"),
            food("Natural yogurt, low fat", DAIRY_EGGS, 63, 5.7, 7.4, 1.0, servingG = 150, servingLabel = "1 pot"),
            food("Skyr, plain", DAIRY_EGGS, 63, 11.0, 4.0, 0.2, servingG = 150, servingLabel = "1 pot", barcode = "2001234000024"),
            food("Cottage cheese", DAIRY_EGGS, 98, 11.1, 3.4, 4.3, servingG = 150, barcode = "2001234000116"),
            food("Quark, fat-free", DAIRY_EGGS, 64, 12.0, 3.8, 0.2, servingG = 125),
            food("High-protein pudding", DAIRY_EGGS, 75, 10.0, 6.0, 1.3, servingG = 200, servingLabel = "1 pot", barcode = "2001234000130"),
            food("Cheddar cheese", DAIRY_EGGS, 403, 24.9, 1.3, 33.1, servingG = 30),
            food("Mozzarella", DAIRY_EGGS, 280, 22.0, 2.2, 20.0, servingG = 60),
            food("Feta cheese", DAIRY_EGGS, 264, 14.2, 4.1, 21.3, servingG = 30),
            food("Parmesan", DAIRY_EGGS, 392, 35.8, 3.2, 25.8, servingG = 10, servingLabel = "1 tbsp grated"),
            food("Halloumi", DAIRY_EGGS, 316, 21.2, 1.6, 25.0, servingG = 80),
            food("Cream cheese, light", DAIRY_EGGS, 149, 7.4, 4.3, 11.0, servingG = 30),

            // ---- Grains, bread & potatoes ----
            food("White rice, cooked", GRAINS_BREAD, 130, 2.7, 28.2, 0.3, fiber = 0.4, servingG = 180, servingLabel = "1 cup"),
            food("Brown rice, cooked", GRAINS_BREAD, 123, 2.7, 25.6, 1.0, fiber = 1.6, servingG = 180, servingLabel = "1 cup"),
            food("Basmati rice, cooked", GRAINS_BREAD, 121, 3.5, 25.2, 0.4, fiber = 0.4, servingG = 180, servingLabel = "1 cup"),
            food("Pasta, cooked", GRAINS_BREAD, 158, 5.8, 30.9, 0.9, fiber = 1.8, servingG = 200),
            food("Wholewheat pasta, cooked", GRAINS_BREAD, 124, 5.3, 26.5, 0.5, fiber = 4.5, servingG = 200),
            food("Egg noodles, cooked", GRAINS_BREAD, 138, 4.5, 25.2, 2.1, fiber = 1.2, servingG = 180),
            food("Couscous, cooked", GRAINS_BREAD, 112, 3.8, 23.2, 0.2, fiber = 1.4, servingG = 150),
            food("Quinoa, cooked", GRAINS_BREAD, 120, 4.4, 21.3, 1.9, fiber = 2.8, servingG = 150),
            food("Oats, rolled", GRAINS_BREAD, 389, 16.9, 66.3, 6.9, fiber = 10.6, servingG = 40, barcode = "2001234000055"),
            food("Porridge made with water", GRAINS_BREAD, 71, 2.5, 12.0, 1.5, fiber = 1.7, servingG = 250, servingLabel = "1 bowl"),
            food("Granola", GRAINS_BREAD, 450, 10.0, 64.0, 17.0, fiber = 7.0, servingG = 45, barcode = "2001234000147"),
            food("Corn flakes", GRAINS_BREAD, 357, 7.5, 84.1, 0.4, fiber = 3.3, servingG = 30),
            food("Wheat biscuit cereal", GRAINS_BREAD, 362, 12.0, 69.0, 2.0, fiber = 10.0, servingG = 38, servingLabel = "2 biscuits"),
            food("White bread", GRAINS_BREAD, 265, 9.0, 49.0, 3.2, fiber = 2.7, servingG = 36, servingLabel = "1 slice"),
            food("Wholemeal bread", GRAINS_BREAD, 252, 12.4, 42.7, 3.5, fiber = 6.0, servingG = 38, servingLabel = "1 slice"),
            food("Sourdough bread", GRAINS_BREAD, 272, 10.8, 51.9, 2.4, fiber = 2.2, servingG = 50, servingLabel = "1 slice"),
            food("Bagel, plain", GRAINS_BREAD, 250, 10.0, 48.9, 1.5, fiber = 2.1, servingG = 85, servingLabel = "1 bagel"),
            food("Flour tortilla wrap", GRAINS_BREAD, 306, 8.2, 50.0, 8.0, fiber = 3.5, servingG = 62, servingLabel = "1 wrap"),
            food("Pitta bread", GRAINS_BREAD, 275, 9.1, 55.7, 1.2, fiber = 2.2, servingG = 60, servingLabel = "1 pitta"),
            food("Rice cakes", GRAINS_BREAD, 387, 8.2, 81.5, 2.8, fiber = 4.2, servingG = 9, servingLabel = "1 cake", barcode = "2001234000062"),
            food("Potatoes, boiled", GRAINS_BREAD, 87, 1.9, 20.1, 0.1, fiber = 1.8, servingG = 200),
            food("Sweet potato, baked", GRAINS_BREAD, 90, 2.0, 20.7, 0.2, fiber = 3.3, servingG = 200),

            // ---- Fruit ----
            food("Banana", FRUIT, 89, 1.1, 22.8, 0.3, fiber = 2.6, servingG = 120, servingLabel = "1 medium"),
            food("Apple", FRUIT, 52, 0.3, 13.8, 0.2, fiber = 2.4, servingG = 150, servingLabel = "1 medium"),
            food("Orange", FRUIT, 47, 0.9, 11.8, 0.1, fiber = 2.4, servingG = 130, servingLabel = "1 medium"),
            food("Pear", FRUIT, 57, 0.4, 15.2, 0.1, fiber = 3.1, servingG = 170, servingLabel = "1 medium"),
            food("Kiwi", FRUIT, 61, 1.1, 14.7, 0.5, fiber = 3.0, servingG = 75, servingLabel = "1 kiwi"),
            food("Strawberries", FRUIT, 32, 0.7, 7.7, 0.3, fiber = 2.0, servingG = 150),
            food("Blueberries", FRUIT, 57, 0.7, 14.5, 0.3, fiber = 2.4, servingG = 80),
            food("Raspberries", FRUIT, 52, 1.2, 11.9, 0.7, fiber = 6.5, servingG = 80),
            food("Grapes", FRUIT, 69, 0.7, 18.1, 0.2, fiber = 0.9, servingG = 100),
            food("Pineapple", FRUIT, 50, 0.5, 13.1, 0.1, fiber = 1.4, servingG = 120),
            food("Mango", FRUIT, 60, 0.8, 15.0, 0.4, fiber = 1.6, servingG = 120),
            food("Watermelon", FRUIT, 30, 0.6, 7.6, 0.2, fiber = 0.4, servingG = 250),
            food("Avocado", FRUIT, 160, 2.0, 8.5, 14.7, fiber = 6.7, servingG = 70, servingLabel = "half"),
            food("Dates, dried", FRUIT, 277, 1.8, 75.0, 0.2, fiber = 6.7, servingG = 24, servingLabel = "1 Medjool date"),
            food("Raisins", FRUIT, 299, 3.1, 79.2, 0.5, fiber = 3.7, servingG = 30),

            // ---- Vegetables ----
            food("Broccoli, boiled", VEGETABLES, 35, 2.4, 7.2, 0.4, fiber = 3.3, servingG = 100),
            food("Spinach, raw", VEGETABLES, 23, 2.9, 3.6, 0.4, fiber = 2.2, servingG = 50),
            food("Carrots", VEGETABLES, 41, 0.9, 9.6, 0.2, fiber = 2.8, servingG = 80),
            food("Tomatoes", VEGETABLES, 18, 0.9, 3.9, 0.2, fiber = 1.2, servingG = 100),
            food("Cucumber", VEGETABLES, 15, 0.7, 3.6, 0.1, fiber = 0.5, servingG = 80),
            food("Red pepper", VEGETABLES, 31, 1.0, 6.0, 0.3, fiber = 2.1, servingG = 120, servingLabel = "1 pepper"),
            food("Onion", VEGETABLES, 40, 1.1, 9.3, 0.1, fiber = 1.7, servingG = 80),
            food("Mixed salad leaves", VEGETABLES, 17, 1.2, 3.3, 0.3, fiber = 2.1, servingG = 50),
            food("Green beans, boiled", VEGETABLES, 35, 1.9, 7.9, 0.3, fiber = 3.2, servingG = 100),
            food("Mushrooms", VEGETABLES, 22, 3.1, 3.3, 0.3, fiber = 1.0, servingG = 80),
            food("Courgette", VEGETABLES, 17, 1.2, 3.1, 0.3, fiber = 1.0, servingG = 100),
            food("Cauliflower", VEGETABLES, 25, 1.9, 5.0, 0.3, fiber = 2.0, servingG = 100),
            food("Asparagus", VEGETABLES, 20, 2.2, 3.9, 0.1, fiber = 2.1, servingG = 100),
            food("Sweetcorn", VEGETABLES, 86, 3.3, 19.0, 1.4, fiber = 2.0, servingG = 80),
            food("Peas, boiled", VEGETABLES, 84, 5.4, 15.6, 0.2, fiber = 5.5, servingG = 80),
            food("Mixed vegetables, frozen, cooked", VEGETABLES, 65, 2.9, 13.1, 0.2, fiber = 4.4, servingG = 150),

            // ---- Legumes, nuts & seeds ----
            food("Chickpeas, cooked", LEGUMES_NUTS, 164, 8.9, 27.4, 2.6, fiber = 7.6, servingG = 120),
            food("Lentils, cooked", LEGUMES_NUTS, 116, 9.0, 20.1, 0.4, fiber = 7.9, servingG = 150),
            food("Black beans, cooked", LEGUMES_NUTS, 132, 8.9, 23.7, 0.5, fiber = 8.7, servingG = 120),
            food("Red kidney beans, cooked", LEGUMES_NUTS, 127, 8.7, 22.8, 0.5, fiber = 6.4, servingG = 120),
            food("Baked beans in tomato sauce", LEGUMES_NUTS, 78, 4.7, 16.7, 0.2, fiber = 3.8, servingG = 200, servingLabel = "half can", barcode = "2001234000086"),
            food("Edamame", LEGUMES_NUTS, 121, 11.9, 8.9, 5.2, fiber = 5.2, servingG = 100),
            food("Tofu, firm", LEGUMES_NUTS, 144, 17.3, 2.8, 8.7, fiber = 2.3, servingG = 150),
            food("Hummus", LEGUMES_NUTS, 166, 7.9, 14.3, 9.6, fiber = 6.0, servingG = 50, barcode = "2001234000123"),
            food("Peanut butter", LEGUMES_NUTS, 588, 25.1, 20.0, 50.0, fiber = 6.0, servingG = 16, servingLabel = "1 tbsp", barcode = "2001234000093"),
            food("Peanuts, roasted", LEGUMES_NUTS, 585, 23.7, 21.5, 49.7, fiber = 8.0, servingG = 30),
            food("Almonds", LEGUMES_NUTS, 579, 21.2, 21.6, 49.9, fiber = 12.5, servingG = 30, servingLabel = "small handful"),
            food("Walnuts", LEGUMES_NUTS, 654, 15.2, 13.7, 65.2, fiber = 6.7, servingG = 30),
            food("Cashews", LEGUMES_NUTS, 553, 18.2, 30.2, 43.9, fiber = 3.3, servingG = 30),
            food("Chia seeds", LEGUMES_NUTS, 486, 16.5, 42.1, 30.7, fiber = 34.4, servingG = 15, servingLabel = "1 tbsp"),

            // ---- Fats & oils ----
            food("Olive oil", FATS_OILS, 884, 0, 0, 100.0, servingG = 10, servingLabel = "1 tbsp"),
            food("Sunflower oil", FATS_OILS, 884, 0, 0, 100.0, servingG = 10, servingLabel = "1 tbsp"),
            food("Butter", FATS_OILS, 717, 0.9, 0.1, 81.1, servingG = 10, servingLabel = "1 portion"),
            food("Mayonnaise", FATS_OILS, 680, 1.0, 0.6, 75.0, servingG = 15, servingLabel = "1 tbsp"),

            // ---- Snacks & sweets ----
            food("Protein bar", SNACKS_SWEETS, 360, 33.0, 35.0, 10.0, fiber = 6.0, servingG = 60, servingLabel = "1 bar", barcode = "2001234000031"),
            food("Granola bar", SNACKS_SWEETS, 471, 10.1, 64.4, 19.8, fiber = 5.3, servingG = 40, servingLabel = "1 bar"),
            food("Dark chocolate 70%", SNACKS_SWEETS, 598, 7.8, 45.9, 42.6, fiber = 10.9, servingG = 20, servingLabel = "2 squares"),
            food("Milk chocolate", SNACKS_SWEETS, 535, 7.7, 59.4, 29.7, fiber = 3.4, servingG = 45, servingLabel = "1 bar"),
            food("Potato crisps", SNACKS_SWEETS, 536, 7.0, 53.0, 34.6, fiber = 4.4, servingG = 40, servingLabel = "1 bag"),
            food("Popcorn, air-popped", SNACKS_SWEETS, 387, 12.9, 77.8, 4.5, fiber = 14.5, servingG = 25),
            food("Digestive biscuit", SNACKS_SWEETS, 480, 7.1, 66.2, 21.5, fiber = 3.6, servingG = 15, servingLabel = "1 biscuit"),
            food("Croissant", SNACKS_SWEETS, 406, 8.2, 45.8, 21.0, fiber = 2.6, servingG = 60, servingLabel = "1 croissant"),
            food("Blueberry muffin", SNACKS_SWEETS, 377, 5.0, 54.0, 16.0, fiber = 1.5, servingG = 110, servingLabel = "1 muffin"),
            food("Vanilla ice cream", SNACKS_SWEETS, 207, 3.5, 23.6, 11.0, fiber = 0.7, servingG = 70, servingLabel = "1 scoop"),
            food("Gummy sweets", SNACKS_SWEETS, 330, 6.0, 77.0, 0, servingG = 30),
            food("Honey", SNACKS_SWEETS, 304, 0.3, 82.4, 0, fiber = 0.2, servingG = 21, servingLabel = "1 tbsp"),
            food("Strawberry jam", SNACKS_SWEETS, 250, 0.4, 62.0, 0.1, fiber = 1.0, servingG = 15, servingLabel = "1 tbsp"),
            food("White sugar", SNACKS_SWEETS, 387, 0, 100.0, 0, servingG = 4, servingLabel = "1 tsp"),

            // ---- Drinks (1 ml ≈ 1 g) ----
            food("Orange juice", DRINKS, 45, 0.7, 10.4, 0.2, fiber = 0.2, servingG = 250, servingLabel = "1 glass"),
            food("Fruit smoothie", DRINKS, 54, 0.6, 13.0, 0.2, fiber = 1.0, servingG = 250, servingLabel = "1 bottle"),
            food("Cola", DRINKS, 42, 0, 10.6, 0, servingG = 330, servingLabel = "1 can"),
            food("Cola, zero sugar", DRINKS, 0.3, 0, 0, 0, servingG = 330, servingLabel = "1 can"),
            food("Coffee, black", DRINKS, 2, 0.1, 0, 0, servingG = 250, servingLabel = "1 mug"),
            food("Latte, semi-skimmed milk", DRINKS, 44, 2.9, 4.4, 1.6, servingG = 350, servingLabel = "1 regular"),
            food("Oat drink", DRINKS, 46, 1.0, 6.8, 1.5, fiber = 0.8, servingG = 250, servingLabel = "1 glass"),
            food("Soya drink, unsweetened", DRINKS, 33, 3.0, 0.7, 1.8, fiber = 0.6, servingG = 250, servingLabel = "1 glass"),
            food("Isotonic sports drink", DRINKS, 26, 0, 6.4, 0, servingG = 500, servingLabel = "1 bottle"),

            // ---- Supplements ----
            food("Whey protein powder", SUPPLEMENTS, 380, 75.0, 6.7, 5.0, servingG = 30, servingLabel = "1 scoop", barcode = "2001234000048"),
            food("Casein protein powder", SUPPLEMENTS, 360, 80.0, 5.0, 1.7, servingG = 30, servingLabel = "1 scoop"),

            // ---- Ready meals & takeaway ----
            food("Chicken curry with rice", READY_MEALS, 145, 8.0, 18.0, 4.5, fiber = 1.2, servingG = 400, servingLabel = "1 meal"),
            food("Spaghetti bolognese", READY_MEALS, 135, 7.0, 16.0, 4.5, fiber = 1.5, servingG = 400, servingLabel = "1 meal"),
            food("Shepherd's pie", READY_MEALS, 112, 6.0, 10.5, 5.0, fiber = 1.2, servingG = 350, servingLabel = "1 meal"),
            food("Chicken stir-fry with vegetables", READY_MEALS, 110, 11.0, 6.0, 4.5, fiber = 1.5, servingG = 350, servingLabel = "1 plate"),
            food("Chicken burrito", READY_MEALS, 190, 10.0, 22.0, 6.8, fiber = 2.5, servingG = 300, servingLabel = "1 burrito"),
            food("Chicken salad wrap", READY_MEALS, 190, 11.5, 20.0, 7.0, fiber = 2.0, servingG = 220, servingLabel = "1 wrap"),
            food("Tuna mayo sandwich", READY_MEALS, 225, 11.0, 24.0, 9.5, fiber = 2.0, servingG = 180, servingLabel = "1 sandwich"),
            food("Chicken Caesar salad", READY_MEALS, 127, 9.0, 4.5, 8.0, fiber = 1.0, servingG = 300, servingLabel = "1 bowl"),
            food("Overnight oats with milk", READY_MEALS, 135, 5.5, 20.0, 3.5, fiber = 2.5, servingG = 250, servingLabel = "1 jar"),
            food("Chicken noodle soup", READY_MEALS, 36, 2.4, 4.3, 1.0, fiber = 0.3, servingG = 300, servingLabel = "1 bowl"),
            food("Margherita pizza", READY_MEALS, 266, 11.4, 33.3, 9.7, fiber = 2.3, servingG = 107, servingLabel = "1 slice"),
            food("Beef burger in a bun", READY_MEALS, 250, 13.0, 24.0, 11.0, fiber = 1.5, servingG = 220, servingLabel = "1 burger"),
            food("French fries", READY_MEALS, 312, 3.4, 41.4, 14.7, fiber = 3.8, servingG = 150, servingLabel = "1 medium portion"),
            food("Chicken nuggets", READY_MEALS, 296, 15.3, 18.0, 19.0, fiber = 1.0, servingG = 100, servingLabel = "6 pieces"),
            food("Salmon nigiri", READY_MEALS, 145, 6.5, 24.0, 2.5, fiber = 0.3, servingG = 30, servingLabel = "1 piece"),

            // ---- Sauces & condiments ----
            food("Tomato ketchup", SAUCES, 101, 1.0, 27.4, 0.1, fiber = 0.3, servingG = 15, servingLabel = "1 tbsp"),
            food("Soy sauce", SAUCES, 53, 8.1, 4.9, 0.6, fiber = 0.8, servingG = 15, servingLabel = "1 tbsp"),
            food("Basil pesto", SAUCES, 430, 5.0, 6.0, 43.0, fiber = 1.5, servingG = 30),
            food("Tomato pasta sauce", SAUCES, 50, 1.5, 8.0, 1.5, fiber = 1.5, servingG = 125),
            food("Sweet chilli sauce", SAUCES, 230, 0.4, 56.0, 0.3, fiber = 1.0, servingG = 15, servingLabel = "1 tbsp"),
        )
    }
}
