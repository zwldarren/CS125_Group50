def calculate_bmr(weight, height, age, gender):
    if gender == 'male':
        bmr = (10 * weight + 6.25 * height - 5 * age + 5) * 1000
    else:  # female
        bmr = (10 * weight + 6.25 * height - 5 * age - 161) * 1000
    return bmr


def calculate_tdee(bmr, average_calories_burned_per_week):
    # Considering that sports watches record including light activities like walking, the main criterion is the daily average calorie burn
    daily_average_calories_burned = average_calories_burned_per_week / 7
    if daily_average_calories_burned < 200000:
        activity_level = 'sedentary'
    elif 200000 <= daily_average_calories_burned < 400000:
        activity_level = 'light'
    elif 400000 <= daily_average_calories_burned < 600000:
        activity_level = 'moderate'
    elif 600000 <= daily_average_calories_burned < 800000:
        activity_level = 'active'
    else:
        activity_level = 'very_active'

    activity_factors = {
        'sedentary': 1.2,
        'light': 1.375,
        'moderate': 1.55,
        'active': 1.725,
        'very_active': 1.9,
    }

    return bmr * activity_factors[activity_level]


def calculate_score(tdee, calorie_intake, calorie_burn):
    # print(calorie_intake / 1000, tdee / 1000, calorie_burn / 1000)
    calorie_difference = calorie_intake - (tdee + calorie_burn)
    abs_diff = abs(calorie_difference)

    # Set maximum difference threshold, score is 0 beyond this value
    max_diff = 2000000

    # Calculate score, the greater the difference, the lower the score
    score = max(0, 10 - (abs_diff / max_diff) * 10)

    return score, calorie_difference


def get_exercise_score(weight, height, age, gender, average_calories_burned_per_week, calorie_intake, calorie_burn):
    # Function to calculate the total score
    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    fin_score, fin_calorie_difference = calculate_score(tdee, calorie_intake, calorie_burn)
    print(
        f"in kcal: BMR: {bmr / 1000:.2f}, TDEE: {tdee / 1000:.2f}, last week: {average_calories_burned_per_week / 1000:.2f}, Score: {fin_score:.2f}, Calorie difference: {fin_calorie_difference / 1000:.2f}, calorie_intake: {calorie_intake / 1000:.2f}, calorie_burn: {calorie_burn / 1000:.2f}")
    return fin_score, fin_calorie_difference


if __name__ == "__main__":
    # Example user data
    weight = 70  # kg
    height = 175  # cm
    age = 30
    gender = 'male'
    average_calories_burned_per_week = 1400000  # Total calories burned in exercise last week
    calorie_intake = 2500000  # Daily calorie intake
    calorie_burn = 200000  # Daily calories burned through exercise

    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    # score, calorie_difference = calculate_score(tdee, calorie_intake, calorie_burn)

    # Call the function to calculate score and total
    fin_score, fin_calorie_difference = get_exercise_score(weight, height, age, gender,
                                                           average_calories_burned_per_week, calorie_intake,
                                                           calorie_burn)

    print(
        f"in cal: BMR: {bmr:.2f}, TDEE: {tdee:.2f}, Score: {fin_score:.2f}, Calorie difference: {fin_calorie_difference:.2f}")
    print(
        f"in kcal: BMR: {bmr / 1000:.2f}, TDEE: {tdee / 1000:.2f}, Score: {fin_score:.2f}, Calorie difference: {fin_calorie_difference / 1000:.2f}")
    print(f"Exercise Score: {fin_score:.2f}")

