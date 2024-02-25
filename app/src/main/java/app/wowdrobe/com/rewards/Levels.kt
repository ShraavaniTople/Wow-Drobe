package app.wowdrobe.com.rewards

data class Level(val name: String, val number: Int, val pointOfAchievements: Int)

val levels = mutableListOf(
    Level("Eco Starter", 1, 50),
    Level("Recycling Rookie", 2, 120),
    Level("Waste Warrior", 3, 230),
    Level("Eco Enthusiast", 4, 400),
    Level("Green Advocate", 5, 650),
    Level("Sustainability Supporter", 6, 1000),
    Level("Eco Champion", 7, 1450),
    Level("Waste Reduction Pro", 8, 2000),
    Level("Green Innovator", 9, 2650),
    Level("Eco Leader", 10, 3400),
    Level("Sustainable Visionary", 11, 4250),
    Level("Eco Entrepreneur", 12, 5100),
    Level("Waste-to-Wealth Pioneer", 13, 5950),
    Level("Sustainability Maestro", 14, 6800),
    Level("Eco Pioneer", 15, 7650),
    Level("Green Guru", 16, 8500),
    Level("Waste Wizard", 17, 9350),
    Level("Eco Innovator", 18, 10200),
    Level("Sustainability Icon", 19, 11050),
    Level("Eco Mastermind", 20, 11900),
    Level("Zero Waste Hero", 21, 12750),
    Level("Waste-to-Wealth Mogul", 22, 13600),
    Level("Sustainable Legend", 23, 14450),
    Level("Eco Tycoon", 24, 15300),
    Level("Green Visionary", 25, 16150),
    Level("Waste-to-Wealth Icon", 26, 17000),
    Level("Sustainability Guru", 27, 17850),
    Level("Eco Savior", 28, 18700),
    Level("Planet Protector", 29, 19550),
    Level("Sustainable Superstar", 30, 5000)
)

// Taglines for Unlocking New Levels
val unlockLevelTaglines = listOf(
    "Congratulations on unlocking a new level!",
    "You've leveled up in your journey to success!",
    "Keep up the good work, you've reached a new milestone!",
    "New level unlocked! Keep the momentum going!",
    "Well done! Your progress is reaching new heights!",
    "You're climbing the ladder of achievement!",
    "Another level conquered! What's next for you?",
    "Each level brings new opportunities and challenges!",
    "Your dedication is paying off. Keep it up!",
    "The sky's the limit, and you're soaring high!"
)

// Taglines for Motivating User to Complete Next Level
val motivateNextLevelTaglines = listOf(
    "You're almost there! The next level awaits your success!",
    "Keep pushing forward to conquer the next level!",
    "One step closer to the next level of greatness!",
    "Success is just one level away. You can do it!",
    "The next level is within reach. Don't stop now!",
    "Unlock the potential of the next level with your determination!",
    "Challenge accepted! Show the next level what you've got!",
    "Your journey is incomplete without conquering the next level!",
    "Don't settle for less. Aim for the next level of excellence!",
    "The next level is calling your name. Keep going!"
)

