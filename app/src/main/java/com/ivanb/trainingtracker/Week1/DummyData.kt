package com.ivanb.trainingtracker.Week1

object DummyData {

    val workouts = listOf(
        Workout(
            id = 1,
            name = "Push Day",
            dateMillis = System.currentTimeMillis(),
            exercises = listOf(
                Exercise("Bench Press", sets = 4, reps = 8, weightKg = 60.0),
                Exercise("Shoulder Press", sets = 3, reps = 10, weightKg = 25.0)
            )
        ),
        Workout(
            id = 2,
            name = "Pull Day",
            dateMillis = System.currentTimeMillis(),
            exercises = listOf(
                Exercise("Deadlift", sets = 3, reps = 5, weightKg = 100.0),
                Exercise("Pull-up", sets = 4, reps = 8, weightKg = 0.0)
            )
        ),
        Workout(
            id = 3,
            name = "Leg Day",
            dateMillis = System.currentTimeMillis(),
            exercises = listOf(
                Exercise("Squat", sets = 4, reps = 6, weightKg = 80.0)
            )
        ),
        Workout(
            id = 4,
            name = "Rest Day",
            dateMillis = System.currentTimeMillis(),
            exercises = listOf()
        )
    )

}