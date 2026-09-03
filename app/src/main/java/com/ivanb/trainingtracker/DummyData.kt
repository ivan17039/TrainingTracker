package com.ivanb.trainingtracker

object DummyData {

    val workouts = listOf(
        Workout(
            id = 1,
            name = "Push Day",
            date = "02.09.2026.",
            exercises = listOf(
                Exercise("Bench Press", sets = 4, reps = 8, weightKg = 60.0),
                Exercise("Shoulder Press", sets = 3, reps = 10, weightKg = 25.0)
            )
        ),
        Workout(
            id = 2,
            name = "Pull Day",
            date = "31.08.2026.",
            exercises = listOf(
                Exercise("Deadlift", sets = 3, reps = 5, weightKg = 100.0),
                Exercise("Pull-up", sets = 4, reps = 8, weightKg = 0.0)
            )
        ),
        Workout(
            id = 3,
            name = "Leg Day",
            date = "29.08.2026.",
            exercises = listOf(
                Exercise("Squat", sets = 4, reps = 6, weightKg = 80.0)
            )
        )
    )

}