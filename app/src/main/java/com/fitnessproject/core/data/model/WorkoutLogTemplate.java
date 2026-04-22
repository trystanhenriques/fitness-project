// Example Template (Do not add unless required for future entities)
//
// @Entity(
//     tableName = "workout_logs",
//     foreignKeys = @ForeignKey(
//         entity = UserAccount.class,
//         parentColumns = "userId",
//         childColumns = "userId",
//         onDelete = ForeignKey.CASCADE
//     ),
//     indices = {@Index("userId")}
// )
// public class WorkoutLog {
//     @PrimaryKey(autoGenerate = true)
//     public Long workoutId;
//
//     // Foreign link: allows NULL if guest flow is permissible offline cleanly
//     public Long userId;
//
//     public String exerciseName;
// }
