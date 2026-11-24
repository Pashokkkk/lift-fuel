package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.Equipment
import com.example.liftfuel.data.local.entity.Equipment.BARBELL
import com.example.liftfuel.data.local.entity.Equipment.BODYWEIGHT
import com.example.liftfuel.data.local.entity.Equipment.CABLE
import com.example.liftfuel.data.local.entity.Equipment.DUMBBELL
import com.example.liftfuel.data.local.entity.Equipment.KETTLEBELL
import com.example.liftfuel.data.local.entity.Equipment.MACHINE
import com.example.liftfuel.data.local.entity.Exercise
import com.example.liftfuel.data.local.entity.MuscleGroup
import com.example.liftfuel.data.local.entity.MuscleGroup.BACK
import com.example.liftfuel.data.local.entity.MuscleGroup.BICEPS
import com.example.liftfuel.data.local.entity.MuscleGroup.CALVES
import com.example.liftfuel.data.local.entity.MuscleGroup.CHEST
import com.example.liftfuel.data.local.entity.MuscleGroup.CORE
import com.example.liftfuel.data.local.entity.MuscleGroup.FULL_BODY
import com.example.liftfuel.data.local.entity.MuscleGroup.GLUTES
import com.example.liftfuel.data.local.entity.MuscleGroup.HAMSTRINGS
import com.example.liftfuel.data.local.entity.MuscleGroup.QUADS
import com.example.liftfuel.data.local.entity.MuscleGroup.SHOULDERS
import com.example.liftfuel.data.local.entity.MuscleGroup.TRICEPS

/** Built-in exercise library (48 common gym exercises). */
object ExerciseSeed {

    fun exercises(): List<Exercise> {
        fun ex(
            name: String,
            primary: MuscleGroup,
            equipment: Equipment,
            compound: Boolean,
            incrementKg: Double,
            cue: String,
            vararg secondary: MuscleGroup,
        ) = Exercise(
            name = name,
            primaryMuscle = primary,
            secondaryMuscles = secondary.toList(),
            equipment = equipment,
            isCompound = compound,
            weightIncrementKg = incrementKg,
            instructions = cue,
        )

        return listOf(
            // ---- Chest ----
            ex("Barbell bench press", CHEST, BARBELL, true, 2.5, "Feet flat, shoulder blades squeezed, touch mid-chest.", TRICEPS, SHOULDERS),
            ex("Incline barbell bench press", CHEST, BARBELL, true, 2.5, "Bench at 30°, lower to upper chest.", SHOULDERS, TRICEPS),
            ex("Dumbbell bench press", CHEST, DUMBBELL, true, 2.0, "Lower the dumbbells to chest level, elbows at 45°.", TRICEPS, SHOULDERS),
            ex("Incline dumbbell press", CHEST, DUMBBELL, true, 2.0, "Bench at 30°, press up and slightly in.", SHOULDERS, TRICEPS),
            ex("Chest press machine", CHEST, MACHINE, true, 5.0, "Handles at mid-chest height, press without locking elbows.", TRICEPS),
            ex("Cable fly", CHEST, CABLE, false, 2.5, "Slight bend in the elbows, hug a big tree."),
            ex("Push-up", CHEST, BODYWEIGHT, true, 0.0, "Body in a straight line, chest to the floor.", TRICEPS, SHOULDERS, CORE),
            ex("Dips", CHEST, BODYWEIGHT, true, 0.0, "Lean forward slightly, lower until upper arms are parallel.", TRICEPS, SHOULDERS),

            // ---- Back ----
            ex("Deadlift", BACK, BARBELL, true, 5.0, "Bar over mid-foot, flat back, push the floor away.", HAMSTRINGS, GLUTES),
            ex("Barbell row", BACK, BARBELL, true, 2.5, "Hinge to 45°, pull the bar to your lower ribs.", BICEPS),
            ex("Lat pulldown", BACK, CABLE, true, 2.5, "Pull the bar to the top of your chest, elbows down.", BICEPS),
            ex("Seated cable row", BACK, CABLE, true, 2.5, "Chest up, pull to your stomach, squeeze shoulder blades.", BICEPS),
            ex("One-arm dumbbell row", BACK, DUMBBELL, true, 2.0, "Hand and knee on the bench, pull the dumbbell to your hip.", BICEPS),
            ex("Pull-up", BACK, BODYWEIGHT, true, 0.0, "Overhand grip, chin over the bar, full hang at the bottom.", BICEPS),
            ex("Chin-up", BACK, BODYWEIGHT, true, 0.0, "Underhand grip, chest towards the bar.", BICEPS),

            // ---- Shoulders ----
            ex("Overhead press", SHOULDERS, BARBELL, true, 2.5, "Squeeze glutes, press straight up, head through at the top.", TRICEPS),
            ex("Seated dumbbell shoulder press", SHOULDERS, DUMBBELL, true, 2.0, "Back against the pad, press without clanking the dumbbells.", TRICEPS),
            ex("Lateral raise", SHOULDERS, DUMBBELL, false, 1.0, "Lead with the elbows, raise to shoulder height."),
            ex("Face pull", SHOULDERS, CABLE, false, 2.5, "Rope at face height, pull towards your eyes, elbows high.", BACK),
            ex("Rear delt fly machine", SHOULDERS, MACHINE, false, 2.5, "Arms almost straight, open wide and squeeze.", BACK),

            // ---- Biceps ----
            ex("Barbell curl", BICEPS, BARBELL, false, 2.5, "Elbows by your sides, no swinging."),
            ex("Dumbbell curl", BICEPS, DUMBBELL, false, 1.0, "Turn the palms up as you curl."),
            ex("Hammer curl", BICEPS, DUMBBELL, false, 1.0, "Palms facing each other throughout."),
            ex("Cable curl", BICEPS, CABLE, false, 2.5, "Stand tall, keep tension at the bottom."),
            ex("Preacher curl machine", BICEPS, MACHINE, false, 2.5, "Armpits on the pad, lower slowly."),

            // ---- Triceps ----
            ex("Triceps pushdown", TRICEPS, CABLE, false, 2.5, "Elbows pinned to your sides, straighten fully."),
            ex("Overhead cable triceps extension", TRICEPS, CABLE, false, 2.5, "Face away from the stack, stretch behind your head."),
            ex("Skull crusher", TRICEPS, BARBELL, false, 2.5, "Lower an EZ bar to your forehead, elbows still."),
            ex("Close-grip bench press", TRICEPS, BARBELL, true, 2.5, "Hands shoulder-width apart, elbows tucked.", CHEST),

            // ---- Quads ----
            ex("Back squat", QUADS, BARBELL, true, 5.0, "Brace, sit between your heels, knees follow your toes.", GLUTES, HAMSTRINGS, CORE),
            ex("Front squat", QUADS, BARBELL, true, 2.5, "Elbows high, torso upright.", GLUTES, CORE),
            ex("Leg press", QUADS, MACHINE, true, 10.0, "Feet shoulder-width, lower until knees reach 90°.", GLUTES),
            ex("Hack squat", QUADS, MACHINE, true, 5.0, "Back flat on the pad, control the descent.", GLUTES),
            ex("Leg extension", QUADS, MACHINE, false, 2.5, "Pause at the top, lower slowly."),
            ex("Bulgarian split squat", QUADS, DUMBBELL, true, 2.0, "Rear foot on the bench, front knee over the toes.", GLUTES),
            ex("Goblet squat", QUADS, DUMBBELL, true, 2.0, "Hold the dumbbell at your chest, elbows inside the knees.", GLUTES),
            ex("Walking lunge", QUADS, DUMBBELL, true, 2.0, "Long steps, back knee almost touches the floor.", GLUTES, HAMSTRINGS),

            // ---- Hamstrings ----
            ex("Romanian deadlift", HAMSTRINGS, BARBELL, true, 2.5, "Soft knees, push the hips back, bar close to your legs.", GLUTES, BACK),
            ex("Lying leg curl", HAMSTRINGS, MACHINE, false, 2.5, "Hips down on the pad, curl to your glutes."),
            ex("Seated leg curl", HAMSTRINGS, MACHINE, false, 2.5, "Thigh pad tight, full range of motion."),

            // ---- Glutes ----
            ex("Hip thrust", GLUTES, BARBELL, true, 5.0, "Upper back on the bench, chin tucked, squeeze at the top.", HAMSTRINGS),
            ex("Cable kickback", GLUTES, CABLE, false, 2.5, "Hinge slightly, kick back without arching your lower back."),

            // ---- Calves ----
            ex("Standing calf raise", CALVES, MACHINE, false, 5.0, "Full stretch at the bottom, pause at the top."),
            ex("Seated calf raise", CALVES, MACHINE, false, 5.0, "Knees under the pad, slow reps."),

            // ---- Core ----
            ex("Hanging leg raise", CORE, BODYWEIGHT, false, 0.0, "No swinging, raise knees or straight legs."),
            ex("Cable crunch", CORE, CABLE, false, 2.5, "Kneel, crunch your ribs towards your hips."),
            ex("Ab wheel rollout", CORE, BODYWEIGHT, false, 0.0, "Brace hard, roll out only as far as you can control."),

            // ---- Full body ----
            ex("Kettlebell swing", FULL_BODY, KETTLEBELL, true, 4.0, "Hinge, snap the hips, let the bell float to chest height.", GLUTES, HAMSTRINGS),
        )
    }
}
