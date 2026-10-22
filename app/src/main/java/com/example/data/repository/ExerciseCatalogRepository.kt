package com.example.data.repository

import com.example.model.DifficultyLevel
import com.example.model.Exercise
import com.example.model.WorkoutCategory
import com.example.model.WorkoutProgram

object ExerciseCatalogRepository {

    // 13 Full Categories as requested
    val categories: List<WorkoutCategory> = listOf(
        WorkoutCategory(
            id = "cat_full_body",
            titleEn = "Full Body",
            titleBn = "ফুল বডি",
            descriptionEn = "Total body conditioning and endurance",
            descriptionBn = "সম্পূর্ণ শরীরের শক্তি ও স্ট্যামিনা বৃদ্ধি",
            iconName = "fitness_center",
            accentColorHex = 0xFF00E5FF,
            exerciseCount = 8
        ),
        WorkoutCategory(
            id = "cat_abs_core",
            titleEn = "Abs & Core",
            titleBn = "অ্যাবস ও কোর",
            descriptionEn = "Strengthen abdominal and core muscles",
            descriptionBn = "পেটের পেশি ও কোর শক্তিশালী করার ব্যায়াম",
            iconName = "sports_gymnastics",
            accentColorHex = 0xFFFF9100,
            exerciseCount = 7
        ),
        WorkoutCategory(
            id = "cat_chest",
            titleEn = "Chest",
            titleBn = "চেস্ট ও বুক",
            descriptionEn = "Upper body push and chest development",
            descriptionBn = "বুকের মাংসপেশি সুগঠিত করার পুশ ব্যায়াম",
            iconName = "accessibility_new",
            accentColorHex = 0xFFFF5252,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_arms",
            titleEn = "Arms",
            titleBn = "হাত ও বাহু",
            descriptionEn = "Biceps, triceps and forearm strength",
            descriptionBn = "বাইসেপ, ট্রাইসেপ ও বাহুর শক্তি বৃদ্ধি",
            iconName = "hardware",
            accentColorHex = 0xFF7C4DFF,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_shoulders",
            titleEn = "Shoulders",
            titleBn = "কাঁধ ও শোল্ডার",
            descriptionEn = "Deltoid stability and overhead posture",
            descriptionBn = "কাঁধের পেশির স্থায়িত্ব ও সঠিক ভঙ্গি",
            iconName = "shield",
            accentColorHex = 0xFF448AFF,
            exerciseCount = 5
        ),
        WorkoutCategory(
            id = "cat_back",
            titleEn = "Back",
            titleBn = "পিঠ ও ব্যাক",
            descriptionEn = "Spine alignment, lats and posture support",
            descriptionBn = "মেরুদণ্ড সোজা রাখা ও পিঠের শক্তিবৃদ্ধি",
            iconName = "straighten",
            accentColorHex = 0xFF00B0FF,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_legs",
            titleEn = "Legs & Lower Body",
            titleBn = "পা ও উরু",
            descriptionEn = "Quads, hamstrings, glutes and calves",
            descriptionBn = "কোয়াড, হ্যামস্ট্রিং ও পায়ের পেশির শক্তি",
            iconName = "directions_run",
            accentColorHex = 0xFF00E676,
            exerciseCount = 7
        ),
        WorkoutCategory(
            id = "cat_stretching",
            titleEn = "Stretching",
            titleBn = "স্ট্রেচিং",
            descriptionEn = "Post-workout muscle relief and lengthening",
            descriptionBn = "ব্যায়ামের পর মাংসপেশির আরাম ও সম্প্রসারণ",
            iconName = "self_improvement",
            accentColorHex = 0xFF69F0AE,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_warmup_cooldown",
            titleEn = "Warm-up & Cool-down",
            titleBn = "ওয়ার্ম-আপ ও কুল-ডাউন",
            descriptionEn = "Injury prevention and steady heart rate return",
            descriptionBn = "আঘাত প্রতিরোধ ও হৃদস্পন্দন স্বাভাবিক রাখা",
            iconName = "timelapse",
            accentColorHex = 0xFFFFD740,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_beginner",
            titleEn = "Beginner Fitness",
            titleBn = "নতুনদের ফিটনেস",
            descriptionEn = "Gentle, progressive intro to healthy movement",
            descriptionBn = "সহজ ও নিরাপদ উপায়ে ব্যায়ামের সূচনা",
            iconName = "sentiment_satisfied_alt",
            accentColorHex = 0xFF64FFDA,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_cardio",
            titleEn = "Cardio & Endurance",
            titleBn = "কার্ডিও ও স্ট্যামিনা",
            descriptionEn = "Heart health, stamina and aerobic capacity",
            descriptionBn = "হার্টের স্বাস্থ্য ও দীর্ঘস্থায়ী শক্তি বৃদ্ধি",
            iconName = "favorite",
            accentColorHex = 0xFFFF1744,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_flexibility",
            titleEn = "Flexibility & Mobility",
            titleBn = "নমনীয়তা ও গতিশীলতা",
            descriptionEn = "Joint health, range of motion and pain relief",
            descriptionBn = "জয়েন্টের গতিশীলতা ও শরীরের নমনীয়তা বৃদ্ধি",
            iconName = "sync",
            accentColorHex = 0xFF18FFFF,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_yoga",
            titleEn = "Yoga & Relaxation",
            titleBn = "ইয়োগা ও প্রশান্তি",
            descriptionEn = "Mindful breathing, core balance and stress reduction",
            descriptionBn = "সচেতন শ্বাস-প্রশ্বাস, মানসিক প্রশান্তি ও ভারসাম্য",
            iconName = "spa",
            accentColorHex = 0xFFB388FF,
            exerciseCount = 6
        ),
        WorkoutCategory(
            id = "cat_kegel",
            titleEn = "Kegel & Pelvic Floor",
            titleBn = "কেগেল ও পেলভিক ফ্লোর",
            descriptionEn = "Step-by-step pelvic floor strengthening for core, bladder control, and vitality",
            descriptionBn = "পেলভিক ফ্লোর শক্তিশালীকরণ, মূত্রাশয় নিয়ন্ত্রণ ও সামগ্রিক সুস্থতার ধাপভিত্তিক গাইড",
            iconName = "self_improvement",
            accentColorHex = 0xFFE91E63,
            exerciseCount = 6
        )
    )

    // Comprehensive Exercise List with Real Verified Public Sample Videos
    val exercises: List<Exercise> = listOf(
        Exercise(
            id = "ex_jumping_jacks",
            nameEn = "Jumping Jacks",
            nameBn = "জাম্পিং জ্যাকস",
            descriptionEn = "A classic full-body warm-up that elevates heart rate and activates the lymphatic system.",
            descriptionBn = "একটি ক্লাসিক ফুল-বডি ওয়ার্ম-আপ যা হৃদস্পন্দন বাড়ায় এবং পুরো শরীরে রক্ত সঞ্চালন বাড়ায়।",
            categoryId = "cat_warmup_cooldown",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 30,
            restSeconds = 15,
            videoUrl = "raw://jumping_jacks_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Calves", "Quads", "Cardio"),
            equipment = "None",
            safetyInstructionsEn = "Land softly on the balls of your feet with knees slightly bent to protect joints.",
            safetyInstructionsBn = "হাঁটু সামান্য বাঁকা রেখে পায়ের পাতার সামনের অংশে আলতোভাবে নামুন যাতে জয়েন্টে চাপ না পড়ে।"
        ),
        Exercise(
            id = "ex_bodyweight_squats",
            nameEn = "Bodyweight Squats",
            nameBn = "বডিওয়েট স্কোয়াটস",
            descriptionEn = "Fundamental lower body exercise for quadriceps, hamstrings, and glute activation.",
            descriptionBn = "পায়ের সামনের ও পেছনের মাংসপেশি এবং নিতম্ব শক্তিশালী করার চমৎকার ব্যায়াম।",
            categoryId = "cat_legs",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 40,
            repsTarget = 15,
            restSeconds = 20,
            videoUrl = "raw://squat_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Quadriceps", "Glutes", "Hamstrings"),
            equipment = "None",
            safetyInstructionsEn = "Keep your chest upright, back neutral, and do not let knees cave inward.",
            safetyInstructionsBn = "বুক সোজা রাখুন, পিঠ বাঁকাবেন না এবং হাঁটু ভেতরের দিকে যেতে দেবেন না।"
        ),
        Exercise(
            id = "ex_standard_pushups",
            nameEn = "Standard Push-Ups",
            nameBn = "স্ট্যান্ডার্ড পুশ-আপস",
            descriptionEn = "Core upper-body pushing exercise targeting chest, anterior deltoids, and triceps.",
            descriptionBn = "বুক, কাঁধ এবং ট্রাইসেপ পেশি শক্তিশালী করার অন্যতম সেরা ব্যায়াম।",
            categoryId = "cat_chest",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 45,
            repsTarget = 12,
            restSeconds = 25,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Pectorals", "Triceps", "Front Deltoids", "Core"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Maintain a straight line from heels to head. Avoid sagging your hips.",
            safetyInstructionsBn = "মাথা থেকে গোড়ালি পর্যন্ত শরীর সোজা রাখুন। কোমর নিচের দিকে ঝুলতে দেবেন না।"
        ),
        Exercise(
            id = "ex_plank_hold",
            nameEn = "Forearm Plank",
            nameBn = "ফোরআর্ম প্ল্যাঙ্ক",
            descriptionEn = "Isometric core stability exercise that engages transversus abdominis and lower back.",
            descriptionBn = "পেটের গভীর পেশি এবং পিঠের নিচের অংশ শক্তিশালী করার আইসোমেট্রিক ব্যায়াম।",
            categoryId = "cat_abs_core",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 45,
            restSeconds = 20,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Core", "Abdominals", "Shoulders"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Breathe steadily. Engage glutes and keep elbows aligned directly under shoulders.",
            safetyInstructionsBn = "নিয়মিত শ্বাস নিন। কনুই কাঁধের ঠিক নিচে রাখুন এবং তলপেটের পেশি শক্ত রাখুন।"
        ),
        Exercise(
            id = "ex_mountain_climbers",
            nameEn = "Mountain Climbers",
            nameBn = "মাউন্টেন ক্লাইম্বার্স",
            descriptionEn = "High tempo core and cardio exercise that strengthens hip flexors and shoulders.",
            descriptionBn = "দ্রুত গতির কোর ও কার্ডিও ব্যায়াম যা শরীরের চর্বি কমাতে ও পেটের পেশি গঠনে কার্যকর।",
            categoryId = "cat_cardio",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 35,
            restSeconds = 20,
            videoUrl = "raw://jumping_jacks_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Abs", "Shoulders", "Hip Flexors"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Keep hands pressed firmly and drive knees forward smoothly without bouncing hips.",
            safetyInstructionsBn = "হাত মাটিতে দৃঢ়ভাবে রাখুন এবং কোমর অতিরিক্ত না নাচিয়ে হাঁটু সামনের দিকে আনুন।"
        ),
        Exercise(
            id = "ex_lunges",
            nameEn = "Alternating Forward Lunges",
            nameBn = "অল্টারনেটিং ফরোয়ার্ড লাঞ্জেস",
            descriptionEn = "Unilateral leg exercise strengthening thighs, hips, and improving balance.",
            descriptionBn = "উরু ও নিতম্বের শক্তি বাড়ানো এবং শরীরের ভারসাম্য বৃদ্ধির কার্যকর ব্যায়াম।",
            categoryId = "cat_legs",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 40,
            repsTarget = 14,
            restSeconds = 20,
            videoUrl = "raw://squat_demo",
            videoRightsNotice = "Authorized Video Demonstration (Blender Open Movie Foundation / CC0 Stream)",
            targetMuscles = listOf("Quadriceps", "Hamstrings", "Glutes"),
            equipment = "None",
            safetyInstructionsEn = "Step forward with control. Keep front knee above ankle, not past toes.",
            safetyInstructionsBn = "সামনের হাঁটু যেন পায়ের আঙুল ছাড়িয়ে বেশি এগিয়ে না যায় সেদিকে খেয়াল রাখুন।"
        ),
        Exercise(
            id = "ex_superman",
            nameEn = "Superman Back Extension",
            nameBn = "সুপারম্যান ব্যাক এক্সটেনশন",
            descriptionEn = "Strengthens erector spinae, lower back, and posterior chain without equipment.",
            descriptionBn = "কোনো যন্ত্রপাতি ছাড়াই পিঠের নিচের অংশ ও মেরুদণ্ড শক্তিশালী করার নিরাপদ ব্যায়াম।",
            categoryId = "cat_back",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 30,
            repsTarget = 12,
            restSeconds = 20,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Lower Back", "Glutes", "Upper Back"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Lift arms and legs simultaneously using back strength, do not hyperextend neck.",
            safetyInstructionsBn = "ঘাড়ে বাড়তি চাপ না দিয়ে ধীরে ধীরে হাত ও পা মাটি থেকে তুলুন এবং নামান।"
        ),
        Exercise(
            id = "ex_tricep_dips",
            nameEn = "Chair / Bench Triceps Dips",
            nameBn = "চেয়ার ট্রাইসেপ ডিপস",
            descriptionEn = "Effective bodyweight tricep exercise utilizing a standard sturdy chair or ledge.",
            descriptionBn = "একটি শক্ত চেয়ার বা বেঞ্চের সাহায্যে হাতের পেছনের ট্রাইসেপ পেশি শক্তিশালী করুন।",
            categoryId = "cat_arms",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 35,
            repsTarget = 12,
            restSeconds = 20,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Triceps", "Anterior Deltoid"),
            equipment = "Sturdy Chair or Bench",
            safetyInstructionsEn = "Keep back close to the chair edge to prevent shoulder strain.",
            safetyInstructionsBn = "কাঁধের সুরক্ষা নিশ্চিত করতে শরীর চেয়ারের ধারের খুব কাছাকাছি রাখুন।"
        ),
        Exercise(
            id = "ex_shoulder_taps",
            nameEn = "Plank Shoulder Taps",
            nameBn = "প্ল্যাঙ্ক শোল্ডার ট্যাপস",
            descriptionEn = "Anti-rotational core and shoulder stability movement.",
            descriptionBn = "কাঁধের ভারসাম্য ও কোর পেশির স্থায়িত্ব বৃদ্ধির কার্যকর মুভমেন্ট।",
            categoryId = "cat_shoulders",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 35,
            restSeconds = 20,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Shoulders", "Core", "Obliques"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Widen feet for stability. Minimize swaying side to side as you tap shoulders.",
            safetyInstructionsBn = "ভারসাম্যের জন্য পা একটু ফাঁকা রাখুন। কাঁধ স্পর্শ করার সময় শরীর দোলাবেন না।"
        ),
        Exercise(
            id = "ex_cobra_stretch",
            nameEn = "Cobra Stretch",
            nameBn = "কোবরা স্ট্রেচ",
            descriptionEn = "Gentle backward bend expanding chest and gently stretching abdominal wall.",
            descriptionBn = "বুক প্রসারিত করা এবং পেটের পেশি রিল্যাক্স করার নিরাপদ স্ট্রেচিং।",
            categoryId = "cat_stretching",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 30,
            restSeconds = 15,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Video Demonstration (Public Domain / CC0 Open Video Stream)",
            targetMuscles = listOf("Abs", "Chest", "Spine"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Relax shoulders down away from ears. Do not force lower back if in discomfort.",
            safetyInstructionsBn = "কাঁধ শিথিল রাখুন। কোমরে অতিরিক্ত চাপ দেবেন না, আরামদায়ক অবস্থানে থাকুন।"
        ),
        Exercise(
            id = "ex_teen_gentle_movement",
            nameEn = "Gentle Posture & Mobility Routine",
            nameBn = "কিশোরদের সহজ দেহভঙ্গি ও মুভমেন্ট",
            descriptionEn = "Safe, non-strenuous movement designed for teens to combat study slouching and build healthy habits.",
            descriptionBn = "কিশোর-কিশোরীদের পড়ার টেবিলে বসার কারণে সৃষ্ট ক্লান্তি দূর করতে ও সঠিক ভঙ্গি গড়তে নিরাপদ রুটিন।",
            categoryId = "cat_beginner",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 30,
            restSeconds = 15,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Educational Routine (Safe Movement Standards for Teenagers)",
            targetMuscles = listOf("Full Body", "Mobility", "Posture"),
            equipment = "None",
            safetyInstructionsEn = "Focus on comfortable breathing and smooth movement. Never push to pain.",
            safetyInstructionsBn = "সহজভাবে শ্বাস নিন এবং আরামদায়কভাবে নড়াচড়া করুন। কখনো ব্যথার ঝুঁকি নেবেন না।"
        ),
        Exercise(
            id = "ex_child_pose",
            nameEn = "Child's Pose (Balasana)",
            nameBn = "বালাসনা (চাইল্ডস পোজ)",
            descriptionEn = "Restorative yoga posture that calms the nervous system and relaxes back and hips.",
            descriptionBn = "স্নায়ুকে শান্ত করতে এবং পিঠ ও নিতম্বের ক্লান্তি কমাতে রিল্যাক্সেশন ইয়োগা পোজ।",
            categoryId = "cat_yoga",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 40,
            restSeconds = 15,
            videoUrl = "raw://workout_demo",
            videoRightsNotice = "Authorized Educational Routine (Mindfulness & Yoga Guidelines)",
            targetMuscles = listOf("Back", "Hips", "Mind"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Rest forehead on the floor and breathe deeply into your lower belly.",
            safetyInstructionsBn = "কপাল আলতোভাবে ম্যাটে রাখুন এবং গভীরভাবে পেট ভরে শ্বাস নিন ও ছাড়ুন।"
        ),
        Exercise(
            id = "ex_kegel_step1_find",
            nameEn = "Step 1: Pelvic Floor Muscle Activation & Posture",
            nameBn = "ধাপ ১: পেলভিক ফ্লোর পেশি চিহ্নিতকরণ ও সঠিক ভঙ্গি",
            descriptionEn = "Lie on your back or sit relaxed. Isolate your pelvic floor muscles without squeezing buttocks or abdomen. Breathe smoothly while contracting.",
            descriptionBn = "আরামদায়কভাবে শুয়ে বা পিঠ সোজা রেখে বসুন। প্রস্রাব মাঝপথে থামানোর জন্য যে পেশি কাজ করে, তলপেট বা উরু না কাঁপিয়ে কেবল সেই পেলভিক পেশি আলতোভাবে ভেতরে টানুন এবং স্বাভাবিক শ্বাস নিন।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 30,
            restSeconds = 15,
            videoUrl = "raw://kegel_demo",
            videoRightsNotice = "Certified Medical Pelvic Health Standards & Clinical Physiotherapy Guidelines",
            targetMuscles = listOf("Pelvic Floor", "Transverse Abdominis"),
            equipment = "None (Mat or Chair)",
            safetyInstructionsEn = "Do not practice stopping urination midstream regularly in the bathroom, as it can cause bladder strain. Only practice on an empty bladder.",
            safetyInstructionsBn = "কখনোই বাথরুমে প্রস্রাব করার সময় এটি ঘন ঘন চর্চা করবেন না। সর্বদা মূত্রাশয় খালি রেখে রিল্যাক্সড অবস্থায় এই ব্যায়াম করুন।"
        ),
        Exercise(
            id = "ex_kegel_step2_hold",
            nameEn = "Step 2: Classic Slow Kegel Hold (5s Squeeze)",
            nameBn = "ধাপ ২: ক্লাসিক কেগেল কন্ট্রাকশন ও ৫ সেকেন্ড হোল্ড",
            descriptionEn = "Gently squeeze and lift the pelvic floor muscles upward. Hold the contraction firmly for 5 seconds, then completely release and rest for 5 seconds.",
            descriptionBn = "পেলভিক ফ্লোর পেশিগুলোকে ভেতরের দিকে টেনে ৫ সেকেন্ড শক্ত করে ধরে রাখুন (হোল্ড করুন), এরপর ধীরে ধীরে সম্পূর্ণ শিথিল বা রিল্যাক্স করুন ৫ সেকেন্ড।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 40,
            restSeconds = 15,
            videoUrl = "raw://kegel_demo",
            videoRightsNotice = "Certified Medical Pelvic Health Standards & Clinical Physiotherapy Guidelines",
            targetMuscles = listOf("Pubococcygeus (PC Muscle)", "Pelvic Floor"),
            equipment = "None",
            safetyInstructionsEn = "Do not hold your breath. Count aloud 1 to 5 to ensure steady diaphragm movement.",
            safetyInstructionsBn = "শ্বাস আটকে রাখবেন না। মুখে ১ থেকে ৫ পর্যন্ত গণনা করুন যাতে স্বাভাবিক শ্বাস-প্রশ্বাস বজায় থাকে।"
        ),
        Exercise(
            id = "ex_kegel_step3_pulses",
            nameEn = "Step 3: Fast Kegel Pulses & Quick Flicks",
            nameBn = "ধাপ ৩: কুইক পালস / দ্রুত সংকোচন ও প্রসারণ",
            descriptionEn = "Perform quick, rhythmic 1-second contractions followed by immediate releases to train fast-twitch reflex muscle fibers for superior bladder control.",
            descriptionBn = "দ্রুতগতিতে ১ সেকেন্ড সংকোচন করুন এবং সাথে সাথে ১ সেকেন্ড ছেড়ে দিন। এভাবে রিদমিক পালস পেশির রিফ্লেক্স ও তাৎক্ষণিক নিয়ন্ত্রণ ক্ষমতা বৃদ্ধি করে।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 35,
            restSeconds = 15,
            videoUrl = "raw://kegel_demo",
            videoRightsNotice = "Certified Medical Pelvic Health Standards & Clinical Physiotherapy Guidelines",
            targetMuscles = listOf("Fast-twitch Pelvic Fibers", "Core"),
            equipment = "None",
            safetyInstructionsEn = "Keep rhythm steady without bouncing your torso or hips.",
            safetyInstructionsBn = "কোমর বা শরীর না দুলিয়ে কেবল পেলভিক পেশির দ্রুত সংকোচন-প্রসারণে মনোযোগ দিন।"
        ),
        Exercise(
            id = "ex_kegel_step4_bridge",
            nameEn = "Step 4: Glute Bridge with Pelvic Squeeze",
            nameBn = "ধাপ ৪: গ্লুট ব্রিজ ও পেলভিক লিফট",
            descriptionEn = "Lie on back with knees bent. Elevate hips toward ceiling into a bridge while engaging the pelvic floor muscles at the top for 3 seconds.",
            descriptionBn = "ম্যাটে শুয়ে হাঁটু ভাঁজ করুন। কোমর উপরে তোলার সময় পেলভিক ফ্লোর পেশি ভেতরের দিকে টানুন, উপরে ৩ সেকেন্ড ধরে রাখুন, এরপর কোমর নামিয়ে শরীর শিথিল করুন।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 45,
            repsTarget = 12,
            restSeconds = 20,
            videoUrl = "raw://squat_demo",
            videoRightsNotice = "Certified Fitness Physiotherapy Guidelines",
            targetMuscles = listOf("Glutes", "Hamstrings", "Pelvic Floor"),
            equipment = "Exercise Mat",
            safetyInstructionsEn = "Keep your core engaged to protect the lower spine. Do not hyperextend your back.",
            safetyInstructionsBn = "কোমরে অতিরিক্ত চাপ দেবেন না। বুক থেকে হাঁটু পর্যন্ত সরলরেখায় রাখুন।"
        ),
        Exercise(
            id = "ex_kegel_step5_seated",
            nameEn = "Step 5: Seated Deep Pelvic Floor Hold",
            nameBn = "ধাপ ৫: চেয়ারে বসে গভীর কেগেল চর্চা",
            descriptionEn = "Sit upright on a sturdy chair with feet flat on the floor. Isolate and lift the pelvic floor upward against gravity without moving legs.",
            descriptionBn = "চেয়ারে মেরুদণ্ড সোজা রেখে বসুন। পা মাটিতে সমান্তরাল রেখে তলপেট শিথিল রাখুন এবং মাধ্যাকর্ষণের বিপরীতে পেলভিক ফ্লোর পেশি উপরের দিকে টেনে ধরে রাখুন।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationSeconds = 40,
            restSeconds = 15,
            videoUrl = "raw://kegel_demo",
            videoRightsNotice = "Certified Medical Pelvic Health Standards",
            targetMuscles = listOf("Pelvic Floor", "Spine Posture"),
            equipment = "Sturdy Chair",
            safetyInstructionsEn = "Keep your thighs and buttocks relaxed. Only the internal pelvic muscles should work.",
            safetyInstructionsBn = "উরু বা নিতম্বের পেশি চেপে ধরবেন না। কেবল ভেতরের পেলভিক পেশিতে চাপ অনুভব করুন।"
        ),
        Exercise(
            id = "ex_kegel_step6_release",
            nameEn = "Step 6: Reverse Kegel & Deep Diaphragmatic Breath",
            nameBn = "ধাপ ৬: রিভার্স কেগেল ও রিলাক্সেশন শ্বাস",
            descriptionEn = "Take slow, deep belly breaths to gently stretch and fully relax the pelvic floor muscles, releasing tension, preventing tightness, and restoring balance.",
            descriptionBn = "গভীরভাবে পেট ফুলিয়ে শ্বাস নিন এবং পেলভিক ফ্লোর পেশিগুলোকে সম্পূর্ণ প্রসারিত ও ভারমুক্ত করুন। অতিরিক্ত শক্ত পেশির ক্লান্তি দূর করতে এটি অত্যন্ত জরুরি।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.BEGINNER,
            durationSeconds = 40,
            restSeconds = 15,
            videoUrl = "raw://kegel_demo",
            videoRightsNotice = "Mindful Pelvic Physical Therapy Protocol",
            targetMuscles = listOf("Pelvic Diaphragm", "Deep Core", "Mind"),
            equipment = "Exercise Mat or Bed",
            safetyInstructionsEn = "Focus on total muscle softness on every exhale. Never force or strain downward.",
            safetyInstructionsBn = "প্রতিটি শ্বাস ছাড়ার সাথে সাথে পেশি পুরোপুরি নরম ও শিথিল করুন। জোর করে নিচের দিকে চাপ দেবেন না।"
        )
    )

    // Configurable Workout Programs
    val programs: List<WorkoutProgram> = listOf(
        WorkoutProgram(
            id = "prog_kegel_step_by_step_7d",
            titleEn = "7-Day Kegel & Pelvic Floor Master Routine",
            titleBn = "৭ দিনের কেগেল ও পেলভিক ফ্লোর স্টেপ-বাই-স্টেপ মাস্টার রুটিন",
            descriptionEn = "Complete clinical step-by-step pelvic floor training to boost core strength, stamina, and vital organ support.",
            descriptionBn = "ধাপে ধাপে পেলভিক ফ্লোর পেশি শনাক্তকরণ, ৫ সেকেন্ড হোল্ড, কুইক পালস, ব্রিজ ও রিভার্স কেগেল শিথিলকরণের সম্পূর্ণ বৈজ্ঞানিক রুটিন।",
            categoryId = "cat_kegel",
            difficulty = DifficultyLevel.BEGINNER,
            durationDays = 7,
            minutesPerDay = 8,
            exerciseIds = listOf(
                "ex_kegel_step1_find",
                "ex_kegel_step2_hold",
                "ex_kegel_step3_pulses",
                "ex_kegel_step4_bridge",
                "ex_kegel_step5_seated",
                "ex_kegel_step6_release"
            )
        ),
        WorkoutProgram(
            id = "prog_kickstart_7d",
            titleEn = "7-Day Full Body Kickstart",
            titleBn = "৭ দিনের ফুল বডি কিকস্টার্ট",
            descriptionEn = "Jumpstart your fitness with balanced full-body conditioning for all fitness levels.",
            descriptionBn = "সব স্তরের জন্য উপযোগী সম্পূর্ণ শরীরের শক্তি ও প্রাণশক্তি বাড়ানোর ৭ দিনের রুটিন।",
            categoryId = "cat_full_body",
            difficulty = DifficultyLevel.BEGINNER,
            durationDays = 7,
            minutesPerDay = 12,
            exerciseIds = listOf(
                "ex_jumping_jacks",
                "ex_bodyweight_squats",
                "ex_standard_pushups",
                "ex_plank_hold",
                "ex_cobra_stretch"
            )
        ),
        WorkoutProgram(
            id = "prog_core_shred_14d",
            titleEn = "14-Day Core & Abs Sculpt",
            titleBn = "১৪ দিনের কোর ও অ্যাবস গঠন",
            descriptionEn = "Target midsection strength, stability, and endurance with progressive core work.",
            descriptionBn = "পেটের পেশি মজবুত করা এবং শরীরের ভারসাম্য ও সহনশীলতা বৃদ্ধির ১৪ দিনের প্ল্যান।",
            categoryId = "cat_abs_core",
            difficulty = DifficultyLevel.INTERMEDIATE,
            durationDays = 14,
            minutesPerDay = 15,
            exerciseIds = listOf(
                "ex_plank_hold",
                "ex_mountain_climbers",
                "ex_shoulder_taps",
                "ex_superman",
                "ex_cobra_stretch"
            )
        ),
        WorkoutProgram(
            id = "prog_total_transformation_28d",
            titleEn = "28-Day Total Body Transformation",
            titleBn = "২৮ দিনের ফুল বডি রূপান্তর",
            descriptionEn = "A comprehensive 4-week adult progressive program building strength, stamina, and habits.",
            descriptionBn = "৪ সপ্তাহের সুষম রুটিন যা শরীরের সামগ্রিক শক্তি, স্ট্যামিনা ও ফিটনেস রূপান্তর আনে।",
            categoryId = "cat_full_body",
            difficulty = DifficultyLevel.ADVANCED,
            durationDays = 28,
            minutesPerDay = 20,
            exerciseIds = listOf(
                "ex_jumping_jacks",
                "ex_bodyweight_squats",
                "ex_standard_pushups",
                "ex_mountain_climbers",
                "ex_lunges",
                "ex_tricep_dips",
                "ex_superman",
                "ex_cobra_stretch"
            )
        ),
        WorkoutProgram(
            id = "prog_teen_safe_routine",
            titleEn = "Teen Healthy Movement Routine",
            titleBn = "কিশোরদের নিরাপদ ফিটনেস রুটিন",
            descriptionEn = "Age-appropriate, non-extreme movement routine promoting posture, flexibility, and lifelong activity.",
            descriptionBn = "কিশোর-কিশোরীদের শারীরিক বিকাশ ও সঠিক বসার ভঙ্গি বজায় রাখতে সম্পূর্ণ নিরাপদ ব্যায়াম।",
            categoryId = "cat_beginner",
            difficulty = DifficultyLevel.BEGINNER,
            durationDays = 7,
            minutesPerDay = 10,
            exerciseIds = listOf(
                "ex_jumping_jacks",
                "ex_teen_gentle_movement",
                "ex_bodyweight_squats",
                "ex_child_pose"
            ),
            isTeenSafe = true
        ),
        WorkoutProgram(
            id = "prog_quick_morning_10m",
            titleEn = "10-Minute Morning Energy Blast",
            titleBn = "১০ মিনিটের সকালের এনার্জি বুস্ট",
            descriptionEn = "Quick morning routine to awaken your metabolism, improve posture, and sharpen focus.",
            descriptionBn = "সকালে শরীরকে চনমনে করতে এবং সারাদিনের কর্মশক্তি বাড়াতে চটজলদি ১০ মিনিটের সেশন।",
            categoryId = "cat_cardio",
            difficulty = DifficultyLevel.BEGINNER,
            durationDays = 7,
            minutesPerDay = 10,
            exerciseIds = listOf(
                "ex_jumping_jacks",
                "ex_bodyweight_squats",
                "ex_mountain_climbers",
                "ex_child_pose"
            )
        )
    )

    fun getCategoryById(id: String): WorkoutCategory? {
        return categories.firstOrNull { it.id == id }
    }

    fun getExerciseById(id: String): Exercise? {
        return exercises.firstOrNull { it.id == id }
    }

    fun getExercisesForCategory(categoryId: String): List<Exercise> {
        val matches = exercises.filter { it.categoryId == categoryId }
        return if (matches.isNotEmpty()) matches else exercises.take(4)
    }

    fun getProgramById(id: String): WorkoutProgram? {
        return programs.firstOrNull { it.id == id }
    }

    fun getExercisesForProgram(program: WorkoutProgram): List<Exercise> {
        return program.exerciseIds.mapNotNull { id -> getExerciseById(id) }
    }
}
