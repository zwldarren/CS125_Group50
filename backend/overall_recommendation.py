def calculate_overall_health_score(sleep_score, diet_score, exercise_score, user_goal):
    weights = {'sleep': 0.25, 'diet': 0.25, 'exercise': 0.25}

    weights[user_goal] = 0.5

    overall_score = (sleep_score * weights['sleep'] +
                     diet_score * weights['diet'] +
                     exercise_score * weights['exercise'])
    return overall_score


# base on goal, recommendation
def provide_final_recommendation(overall_score, user_goal):
    recommendations = {
        "sleep, overall score less than 2, and correlation advice": "Significant improvement needed across the board. Prioritize establishing a regular sleep schedule and consider light evening activities like walking to enhance sleep readiness. Nutritionally, focus on avoiding heavy meals and caffeine before bedtime.",
        "sleep, overall score between 2 and 4, and correlation advice": "Room for improvement. Ensure your sleeping environment is optimal for rest, and incorporate relaxation techniques such as meditation or deep breathing exercises. Balanced meals throughout the day can stabilize your energy levels and support better sleep.",
        "sleep, overall score between 4 and 6, and correlation advice": "You're on the right track. Aim for consistent sleep and wake times even on weekends. Exercise regularly but avoid high-intensity workouts close to bedtime. Consider enriching your diet with magnesium and vitamin B6-rich foods to promote better sleep.",
        "sleep, overall score between 6 and 8, and correlation advice": "You're doing well, but there's always room to enhance. Try to limit screen time in the evening and explore mindfulness practices to wind down. A balanced diet, rich in fruits, vegetables, and whole grains, can further improve sleep quality.",
        "sleep, overall score between 8 and 10, and correlation advice": "Excellent health habits. Continue to refine your sleep hygiene by maintaining a cool, dark, and quiet bedroom environment. Experiment with advanced relaxation techniques or yoga for sleep. Maintain your balanced diet and regular exercise routine, ensuring not to overdo caffeine.",

        "exercise, overall score less than 2, and correlation advice": "Start with simple daily activities like taking a brisk 20-minute walk or performing gentle yoga. Eat small, balanced meals every 3-4 hours to maintain energy levels, and aim for 7-8 hours of quality sleep to aid muscle recovery.",
        "exercise, overall score between 2 and 4, and correlation advice": "Incorporate varied moderate-intensity exercises, such as swimming or biking for 30 minutes, 3 times a week. Include protein-rich foods like chicken, fish, or legumes in your diet to support muscle repair, and maintain a sleep routine that allows for enough rest and recovery.",
        "exercise, overall score between 4 and 6, and correlation advice": "Elevate your workout by adding strength training sessions and interval cardio workouts into your weekly routine. Balance your diet with a mix of carbohydrates for energy and antioxidants from fruits and vegetables for recovery. Prioritize sleep by establishing a calming pre-bedtime routine to improve sleep quality.",
        "exercise, overall score between 6 and 8, and correlation advice": "Challenge yourself with higher intensity training, such as HIIT, 4-5 times a week. Optimize your nutrition by timing your intake of complex carbs before workouts and consuming protein within 30 minutes after exercising. Ensure you are getting deep, restorative sleep by minimizing blue light exposure in the evening.",
        "exercise, overall score between 8 and 10, and correlation advice": "Excellent health habits. Maintain peak performance with structured training programs and active recovery days. Tailor your diet to include specific nutrient ratios tailored to your workout intensity and duration—focus on hydration and electrolyte balance. Adopt sleep optimization techniques, such as temperature regulation and limiting disturbances, to maximize recovery and performance.",

        "diet, overall score less than 2, and correlation advice": "Begin by integrating more whole foods into your diet—start with simple swaps like choosing water or herbal teas over sugary drinks, and adding a serving of vegetables to every meal. To complement these changes, take short walks after meals to aid digestion and aim for 7-9 hours of sleep to prevent late-night snacking.",
        "diet, overall score between 2 and 4, and correlation advice": "Focus on meal planning to ensure a balanced intake of nutrients; incorporate lean proteins, whole grains, and healthy fats into each meal. Regular moderate exercise, such as 30 minutes of brisk walking or cycling, can enhance metabolic health. Establishing a consistent sleep schedule can help regulate appetite and reduce cravings.",
        "diet, overall score between 4 and 6, and correlation advice": "Diversify your diet with nutrient-rich foods such as whole grains, lean proteins, and a variety of fruits and vegetables to fuel your body efficiently. Complement this with regular moderate-intensity exercises like brisk walking or yoga, which not only aid in weight management but also promote better digestion and nutrient absorption. Encourage consistent sleep patterns to help regulate metabolism and appetite, ensuring your dietary efforts are more effective.",
        "diet, overall score between 6 and 8, and correlation advice": "Advance your dietary habits by focusing on whole foods and experimenting with meal timings, such as incorporating smaller, more frequent meals to maintain energy levels and metabolism throughout the day. Pair this with a balanced exercise regimen that includes both cardio and strength training to enhance muscle synthesis and fat loss, contributing to a more efficient nutrient use by your body. Quality sleep is essential for muscle recovery and hormonal balance, which supports healthy eating habits and weight management.",
        "diet, overall score between 8 and 10, and correlation advice": "Excellent health habits. Master your dietary approach by fine-tuning your intake based on your body's response, focusing on high-quality proteins, fats, and carbohydrates to support advanced fitness goals. Integrate specific types of exercise that align with your dietary focus, such as endurance training for carbohydrate optimization or resistance training for protein utilization. Optimal sleep becomes crucial at this level, not only for recovery but also for maintaining a balance that allows for the precise dietary control necessary for peak performance."
    }

    advice = []
    if user_goal == "sleep":
        if overall_score <= 2:
            advice.append(recommendations["sleep, overall score less than 2, and correlation advice"])
        elif overall_score <= 4:
            advice.append(recommendations["sleep, overall score between 2 and 4, and correlation advice"])
        elif overall_score <= 6:
            advice.append(recommendations["sleep, overall score between 4 and 6, and correlation advice"])
        elif overall_score <= 8:
            advice.append(recommendations["sleep, overall score between 6 and 8, and correlation advice"])
        elif overall_score <= 10:
            advice.append(recommendations["sleep, overall score between 8 and 10, and correlation advice"])
    elif user_goal == "exercise":
        if overall_score <= 2:
            advice.append(recommendations["exercise, overall score less than 2, and correlation advice"])
        elif overall_score <= 4:
            advice.append(recommendations["exercise, overall score between 2 and 4, and correlation advice"])
        elif overall_score <= 6:
            advice.append(recommendations["exercise, overall score between 4 and 6, and correlation advice"])
        elif overall_score <= 8:
            advice.append(recommendations["exercise, overall score between 6 and 8, and correlation advice"])
        elif overall_score <= 10:
            advice.append(recommendations["exercise, overall score between 8 and 10, and correlation advice"])
    elif user_goal == "diet":
        if overall_score <= 2:
            advice.append(recommendations["diet, overall score less than 2, and correlation advice"])
        elif overall_score <= 4:
            advice.append(recommendations["diet, overall score between 2 and 4, and correlation advice"])
        elif overall_score <= 6:
            advice.append(recommendations["diet, overall score between 4 and 6, and correlation advice"])
        elif overall_score <= 8:
            advice.append(recommendations["diet, overall score between 6 and 8, and correlation advice"])
        elif overall_score <= 10:
            advice.append(recommendations["diet, overall score between 8 and 10, and correlation advice"])

    advice_string = ", ".join(advice)
    return advice_string
