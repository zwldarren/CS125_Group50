def calculate_bmr(weight, height, age, gender):
    if gender == 'male':
        bmr = (10 * weight + 6.25 * height - 5 * age + 5) * 1000
    else:  # female
        bmr = (10 * weight + 6.25 * height - 5 * age - 161) * 1000
    return bmr


def calculate_tdee(bmr, average_calories_burned_per_week):
    # 考虑到运动手表的会记录包括轻微活动如步行，因此主要判断条件是日平均消耗卡路里
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
    print(calorie_intake / 1000, tdee / 1000, calorie_burn / 1000)
    calorie_difference = calorie_intake - (tdee - calorie_burn)
    abs_diff = abs(calorie_difference)

    if abs_diff > 1000000:
        score = 0
    elif abs_diff > 900000:
        score = 1
    elif abs_diff > 800000:
        score = 2
    elif abs_diff > 700000:
        score = 3
    elif abs_diff > 600000:
        score = 4
    elif abs_diff > 500000:
        score = 5
    elif abs_diff > 400000:
        score = 6
    elif abs_diff > 300000:
        score = 7
    elif abs_diff > 200000:
        score = 8
    elif abs_diff > 100000:
        score = 9
    else:
        score = 10  # 健康范围内

    return score, calorie_difference


def get_exercise_score(weight, height, age, gender, average_calories_burned_per_week, calorie_intake, calorie_burn):
    # 计算得分的总函数
    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    fin_score, fin_calorie_difference = calculate_score(tdee, calorie_intake, calorie_burn)
    return fin_score, fin_calorie_difference


if __name__ == "__main__":
    # 示例用户数据
    weight = 70  # kg
    height = 175  # cm
    age = 30
    gender = 'male'
    average_calories_burned_per_week = 1400000  # 用户上周运动消耗的总卡路里
    calorie_intake = 2500000  # 用户每日摄入卡路里
    calorie_burn = 200000  # 用户每日通过运动消耗的卡路里

    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    # score, calorie_difference = calculate_score(tdee, calorie_intake, calorie_burn)

    # call这个计算得分和总函数
    fin_score, fin_calorie_difference = get_exercise_score(weight, height, age, gender,
                                                           average_calories_burned_per_week, calorie_intake,
                                                           calorie_burn)

    print(
        f"in cal: BMR: {bmr:.2f}, TDEE: {tdee:.2f}, Score: {fin_score:.2f}, Calorie difference: {fin_calorie_difference:.2f}")
    print(
        f"in kcal: BMR: {bmr / 1000:.2f}, TDEE: {tdee / 1000:.2f}, Score: {fin_score:.2f}, Calorie difference: {fin_calorie_difference / 1000:.2f}")
    print(f"Exercise Score: {fin_score:.2f}")

