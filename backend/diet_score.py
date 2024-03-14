from datetime import datetime


# Calculate mealtime score
def calculate_mealtime_score(meal_times):
    ideal_times = {
        'breakfast': ('07:00', '09:00'),
        'lunch': ('12:00', '14:00'),
        'dinner': ('18:00', '20:00'),
    }
    meals_met = {'breakfast': False, 'lunch': False, 'dinner': False}

    for actual_time in meal_times:
        for meal, (start, end) in ideal_times.items():
            time_format = '%H:%M'
            actual_dt = datetime.strptime(actual_time, time_format)
            start_dt = datetime.strptime(start, time_format)
            end_dt = datetime.strptime(end, time_format)
            if start_dt <= actual_dt <= end_dt:
                meals_met[meal] = True
                break  # if a time fits one meal, move to the next time

    score = sum(meals_met.values()) * (
                10 / len(ideal_times))  # each meal time met contributes equally to the total score
    return score


# Calculate meal count score
def calculate_meal_count_score(meal_times):
    if not meal_times:
        return 0  # 0 points if no data

    total_meals = len(set(meal_times))  # Use set to remove duplicates, counting each meal time only once
    if total_meals >= 3:
        return 10
    else:
        # Deduct 3.33 points for each missing meal, minimum score is 0
        return max(0.0, 10.0 - (3 - total_meals) * 3.33)


# Calculate calorie burn/intake score
def calculate_bmr(weight, height, age, gender):
    if gender == 'male':
        bmr = (10 * weight + 6.25 * height - 5 * age + 5) * 1000
    else:  # female
        bmr = (10 * weight + 6.25 * height - 5 * age - 161) *1000
    return bmr


def calculate_tdee(bmr, average_calories_burned_per_week):
    # Considering smartwatches record all activities including light movements like walking, main criteria is the daily average calorie burn
    daily_average_calories_burned = average_calories_burned_per_week / 7
    if daily_average_calories_burned < 2000000:
        activity_level = 'sedentary'
    elif 2000000 <= daily_average_calories_burned < 4000000:
        activity_level = 'light'
    elif 4000000 <= daily_average_calories_burned < 6000000:
        activity_level = 'moderate'
    elif 6000000 <= daily_average_calories_burned < 8000000:
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
    calorie_difference = calorie_intake - (tdee + calorie_burn)
    abs_diff = abs(calorie_difference)

    # Set a maximum difference threshold, scores are 0 beyond this value
    max_diff = 2000000

    # The larger the difference, the lower the score
    score = max(0, 10 - (abs_diff / max_diff) * 10)

    return score


def get_calories_difference_score(weight, height, age, gender, average_calories_burned_per_week,
                                  calorie_intake, calorie_burn):
    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    score = calculate_score(tdee, calorie_intake, calorie_burn)

    return score


# Calculate total score
def get_diet_score(meal_times, weight, height, age, gender, average_calories_burned_per_week,
                   calorie_intake, calorie_burn):
    time_score = calculate_mealtime_score(meal_times)
    count_score = calculate_meal_count_score(meal_times)
    calorie_score = get_calories_difference_score(weight, height, age, gender,
                                                  average_calories_burned_per_week, calorie_intake,
                                                  calorie_burn)
    total_diet_score = (calorie_score * 0.7) + (time_score * 0.1) + (count_score * 0.2)
    print(f"diet score: time_score: {time_score:.2f}, count_score: {count_score:.2f}, calorie_score: {calorie_score:.2f}, total_diet_score: {total_diet_score:.2f}")
    return total_diet_score


if __name__ == "__main__":
    # user example
    weight = 70  # kg
    height = 175  # cm
    age = 30
    gender = 'male'

    average_calories_burned_per_week = 1400000  # total calories burn this week
    calorie_intake = 2500000  # calories(not kcal) intake today
    calorie_burn = 200000  # calories(not kcal) burn today

    meal_times = ['08:00', '13:00', '20:00', '20:30', '18:30']

    fin_score = get_diet_score(meal_times, weight, height, age, gender,
                               average_calories_burned_per_week, calorie_intake, calorie_burn)

    print(f"Diet Score: {fin_score:.2f}")

