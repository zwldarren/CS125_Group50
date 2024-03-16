def generate_exercise_advice(exercise_score, weekly_exercise_sessions, calorie_difference, weather):
    actions = {
        "bad weather, exercise score less than 2": "Engage in gentle indoor activities such as stretching or yoga to maintain flexibility and promote relaxation. These low-impact exercises are perfect for beginners.",
        "bad weather, exercise score between 2 and 4": "Explore home-based cardio workouts like jumping jacks, stair climbing, or dance fitness videos online. These exercises don't require much space or equipment and can effectively raise your heart rate.",
        "bad weather, exercise score between 4 and 6": "Set up a home circuit training with bodyweight exercises such as push-ups, sit-ups, and lunges. Rotate between different activities for a comprehensive workout that builds strength and endurance.",
        "bad weather, exercise score between 6 and 8": "If you have access to home gym equipment, focus on a balanced indoor workout combining cardio machines like stationary bikes or treadmills with strength training exercises using weights or resistance bands.",
        "bad weather, exercise score between 8 and 10": "Challenge yourself with advanced indoor workouts such as high-intensity interval training (HIIT) sessions, advanced yoga flows, or martial arts drills. These activities will keep your training intense and engaging, ensuring you stay at peak fitness.",

        "low frequency, exercise score less than 2": "Focus on building a consistent exercise routine with light activities such as daily walks or gentle yoga. Aim for 15-20 minutes per session, 3 times a week to gradually increase your fitness level.",
        "low frequency, exercise score between 2 and 4": "Incorporate moderate aerobic exercises like brisk walking, swimming, or cycling. Start with sessions of 20-30 minutes, at least 3-4 times a week to build endurance and cardiovascular health.",
        "low frequency, exercise score between 4 and 6": "Enhance your routine with a mix of aerobic and strength training activities. Include exercises like running, bodyweight workouts, or resistance training for at least 30 minutes, 4-5 times a week.",
        "low frequency, exercise score between 6 and 8": "Your fitness level allows for more challenging workouts. Try engaging in high-intensity interval training (HIIT), advanced yoga, or sports activities for 30-45 minutes, 5 times a week to push your limits.",
        "low frequency, exercise score between 8 and 10": "As an individual with high fitness, focus on specialized training to further enhance your athletic performance or muscle strength. Consider marathon training, CrossFit, or advanced strength training programs, ensuring to include rest days for recovery.",

        "calorie surplus, exercise score less than 2": "Start with low-impact exercises such as walking or water aerobics. Aim for 30 minutes a day, 3 days a week to begin utilizing the calorie surplus without overwhelming yourself.",
        "calorie surplus, exercise score between 2 and 4": "Incorporate moderate aerobic activities like brisk walking, cycling on flat terrain, or light jogging. Try to engage in these activities for at least 45 minutes, 4 days a week, to help burn off the calorie surplus.",
        "calorie surplus, exercise score between 4 and 6": "Add more vigorous aerobic exercises to your routine, such as running, swimming, or high-intensity cycling. Aim for at least 60 minutes of vigorous activity, 4-5 days a week, to increase calorie expenditure.",
        "calorie surplus, exercise score between 6 and 8": "Integrate high-intensity interval training (HIIT) or circuit training into your weekly routine. These activities are efficient at burning calories and can be done for 30-45 minutes, 5 days a week.",
        "calorie surplus, exercise score between 8 and 10": "Focus on advanced endurance and strength training exercises, like long-distance running, competitive sports, or heavy weightlifting. Engage in these intense activities for 60-90 minutes, 5-6 days a week, to maximize calorie usage and improve athletic performance.",

        "calorie deficit, exercise score less than 2": "Focus on daily mobility and stability exercises to maintain muscle health without significant calorie burn. Try 10-15 minutes of light yoga or stretching each day.",
        "calorie deficit, exercise score between 2 and 4": "Incorporate low-impact aerobic activities that boost your metabolism moderately. Walking or gentle cycling for 20 minutes a day can help manage the calorie deficit effectively.",
        "calorie deficit, exercise score between 4 and 6": "Introduce moderate exercises that support muscle tone and calorie efficiency. Swimming or pilates for 30 minutes, 3-4 days a week, can enhance your fitness without exacerbating the calorie deficit.",
        "calorie deficit, exercise score between 6 and 8": "Engage in strength training sessions to preserve muscle mass, which is crucial during a calorie deficit. Focus on bodyweight exercises or light weightlifting twice a week, accompanied by moderate cardio exercises like brisk walking or cycling on alternate days.",
        "calorie deficit, exercise score between 8 and 10": "With a high fitness level, carefully plan intensive workouts that maintain muscle and cardiovascular health without overconsumption of energy. Mix high-intensity interval training (HIIT) with strength training sessions, ensuring to closely monitor your energy levels and recovery needs.",

        "calorie balanced, frequent, exercise score less than 2": "Given your beginning level, focus on maintaining consistency with low-impact activities such as walking, gentle yoga, or light swimming. Aim for at least 30 minutes per session to gradually build your fitness foundation.",
        "calorie balanced, frequent, exercise score between 2 and 4": "Start incorporating a mix of aerobic exercises like brisk walking or cycling and basic strength training exercises using body weight. Aim for sessions lasting 30 to 45 minutes to enhance endurance and build muscle.",
        "calorie balanced, frequent, exercise score between 4 and 6": "Increase the variety and intensity of your workouts. Include interval training, moderate weightlifting, and dynamic sports activities such as tennis or soccer for 45 to 60 minutes to challenge different muscle groups and improve cardiovascular health.",
        "calorie balanced, frequent, exercise score between 6 and 8": "Your fitness level allows for more advanced training. Engage in high-intensity interval training (HIIT), advanced yoga or Pilates, and heavy weightlifting. Consider working with a trainer to refine technique and push your limits safely for 60 minutes sessions.",
        "calorie balanced, frequent, exercise score between 8 and 10": "As a highly fit individual, focus on specialized or competitive training to further enhance your performance. Explore endurance sports, competitive weightlifting, or challenging fitness classes. Aim to mix high-intensity sessions with adequate rest and recovery practices to optimize performance and prevent injury."

    }

    advice = []

    if weather == "Thunderstorm" or weather == "Rain" or weather == "Snow" or weather == "Drizzle":
        weather_advice = "Given the current weather condition of " + weather + ", it's advisable to avoid outdoor activities. "
        if exercise_score <= 2:
            advice.append(weather_advice + actions["bad weather, exercise score less than 2"])
        elif exercise_score <= 4:
            advice.append(weather_advice + actions["bad weather, exercise score between 2 and 4"])
        elif exercise_score <= 6:
            advice.append(weather_advice + actions["bad weather, exercise score between 4 and 6"])
        elif exercise_score <= 8:
            advice.append(weather_advice + actions["bad weather, exercise score between 6 and 8"])
        elif exercise_score <= 10:
            advice.append(weather_advice + actions["bad weather, exercise score between 8 and 10"])
        advice_string = ", ".join(advice)
        return advice_string
    else:
        weather_advice = f"Current weather {weather} is suitable for outdoor exercise."
        advice.append(weather_advice)

    if calorie_difference >= 2800000:
        if exercise_score <= 2:
            advice.append(actions["calorie surplus, exercise score less than 2"])
        elif exercise_score <= 4:
            advice.append(actions["calorie surplus, exercise score between 2 and 4"])
        elif exercise_score <= 6:
            advice.append(actions["calorie surplus, exercise score between 4 and 6"])
        elif exercise_score <= 8:
            advice.append(actions["calorie surplus, exercise score between 6 and 8"])
        elif exercise_score <= 10:
            advice.append(actions["calorie surplus, exercise score between 8 and 10"])
    elif calorie_difference <= -2800000:
        if exercise_score <= 2:
            advice.append(actions["calorie deficit, exercise score less than 2"])
        elif exercise_score <= 4:
            advice.append(actions["calorie deficit, exercise score between 2 and 4"])
        elif exercise_score <= 6:
            advice.append(actions["calorie deficit, exercise score between 4 and 6"])
        elif exercise_score <= 8:
            advice.append(actions["calorie deficit, exercise score between 6 and 8"])
        elif exercise_score <= 10:
            advice.append(actions["calorie deficit, exercise score between 8 and 10"])
    elif weekly_exercise_sessions < 3:  # Assuming low frequency is less than 3 sessions per week
        if exercise_score <= 2:
            advice.append(actions["low frequency, exercise score less than 2"])
        elif exercise_score <= 4:
            advice.append(actions["low frequency, exercise score between 2 and 4"])
        elif exercise_score <= 6:
            advice.append(actions["low frequency, exercise score between 4 and 6"])
        elif exercise_score <= 8:
            advice.append(actions["low frequency, exercise score between 6 and 8"])
        elif exercise_score <= 10:
            advice.append(actions["low frequency, exercise score between 8 and 10"])
    else:
        if exercise_score <= 2:
            advice.append(actions["calorie balanced, frequent, exercise score less than 2"])
        elif exercise_score <= 4:
            advice.append(actions["calorie balanced, frequent, exercise score between 2 and 4"])
        elif exercise_score <= 6:
            advice.append(actions["calorie balanced, frequent, exercise score between 4 and 6"])
        elif exercise_score <= 8:
            advice.append(actions["calorie balanced, frequent, exercise score between 6 and 8"])
        elif exercise_score <= 10:
            advice.append(actions["calorie balanced, frequent, exercise score between 8 and 10"])



    advice_string = ", ".join(advice)
    return advice_string