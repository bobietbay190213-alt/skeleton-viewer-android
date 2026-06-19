package com.skeletonviewer.domain.models

data class PoseLandmarkModel(
    val x: Float,
    val y: Float,
    val z: Float,
    val visibility: Float,
    val presence: Float
)

data class PoseResultModel(
    val landmarks: List<PoseLandmarkModel>,
    val inferenceTime: Long,
    val inputImageWidth: Int,
    val inputImageHeight: Int
)

enum class LandmarkIndex(val index: Int, val label: String) {
    NOSE(0, "Nose"),
    LEFT_EYE_INNER(1, "L.Eye Inner"),
    LEFT_EYE(2, "Left Eye"),
    LEFT_EYE_OUTER(3, "L.Eye Outer"),
    RIGHT_EYE_INNER(4, "R.Eye Inner"),
    RIGHT_EYE(5, "Right Eye"),
    RIGHT_EYE_OUTER(6, "R.Eye Outer"),
    LEFT_EAR(7, "Left Ear"),
    RIGHT_EAR(8, "Right Ear"),
    MOUTH_LEFT(9, "Mouth L"),
    MOUTH_RIGHT(10, "Mouth R"),
    LEFT_SHOULDER(11, "L.Shoulder"),
    RIGHT_SHOULDER(12, "R.Shoulder"),
    LEFT_ELBOW(13, "L.Elbow"),
    RIGHT_ELBOW(14, "R.Elbow"),
    LEFT_WRIST(15, "L.Wrist"),
    RIGHT_WRIST(16, "R.Wrist"),
    LEFT_PINKY(17, "L.Pinky"),
    RIGHT_PINKY(18, "R.Pinky"),
    LEFT_INDEX(19, "L.Index"),
    RIGHT_INDEX(20, "R.Index"),
    LEFT_THUMB(21, "L.Thumb"),
    RIGHT_THUMB(22, "R.Thumb"),
    LEFT_HIP(23, "L.Hip"),
    RIGHT_HIP(24, "R.Hip"),
    LEFT_KNEE(25, "L.Knee"),
    RIGHT_KNEE(26, "R.Knee"),
    LEFT_ANKLE(27, "L.Ankle"),
    RIGHT_ANKLE(28, "R.Ankle"),
    LEFT_HEEL(29, "L.Heel"),
    RIGHT_HEEL(30, "R.Heel"),
    LEFT_FOOT_INDEX(31, "L.Foot"),
    RIGHT_FOOT_INDEX(32, "R.Foot");

    companion object {
        val BODY_CONNECTIONS: List<Pair<LandmarkIndex, LandmarkIndex>> = listOf(
            // Head
            Pair(LEFT_EAR, LEFT_EYE),
            Pair(LEFT_EYE, NOSE),
            Pair(NOSE, RIGHT_EYE),
            Pair(RIGHT_EYE, RIGHT_EAR),
            // Shoulders
            Pair(LEFT_SHOULDER, RIGHT_SHOULDER),
            // Left Arm
            Pair(LEFT_SHOULDER, LEFT_ELBOW),
            Pair(LEFT_ELBOW, LEFT_WRIST),
            // Right Arm
            Pair(RIGHT_SHOULDER, RIGHT_ELBOW),
            Pair(RIGHT_ELBOW, RIGHT_WRIST),
            // Torso
            Pair(LEFT_SHOULDER, LEFT_HIP),
            Pair(RIGHT_SHOULDER, RIGHT_HIP),
            Pair(LEFT_HIP, RIGHT_HIP),
            // Left Leg
            Pair(LEFT_HIP, LEFT_KNEE),
            Pair(LEFT_KNEE, LEFT_ANKLE),
            Pair(LEFT_ANKLE, LEFT_HEEL),
            Pair(LEFT_HEEL, LEFT_FOOT_INDEX),
            // Right Leg
            Pair(RIGHT_HIP, RIGHT_KNEE),
            Pair(RIGHT_KNEE, RIGHT_ANKLE),
            Pair(RIGHT_ANKLE, RIGHT_HEEL),
            Pair(RIGHT_HEEL, RIGHT_FOOT_INDEX)
        )
    }
}
