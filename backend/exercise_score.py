def calculate_bmr(weight, height, age, gender):
    if gender == 'male':
        bmr = 10 * weight + 6.25 * height - 5 * age + 5
    else:  # female
        bmr = 10 * weight + 6.25 * height - 5 * age - 161
    return bmr


def calculate_tdee(bmr, average_calories_burned_per_week):
    # 考虑到运动手表的会记录包括轻微活动如步行，因此主要判断条件是日平均消耗卡路里
    daily_average_calories_burned = average_calories_burned_per_week / 7
    if daily_average_calories_burned < 200:
        activity_level = 'sedentary'
    elif 200 <= daily_average_calories_burned < 400:
        activity_level = 'light'
    elif 400 <= daily_average_calories_burned < 600:
        activity_level = 'moderate'
    elif 600 <= daily_average_calories_burned < 800:
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
    calorie_difference = calorie_intake - (tdee - calorie_burn)
    abs_diff = abs(calorie_difference)

    if abs_diff > 2000:
        score = 10
    elif abs_diff > 1800:
        score = 9
    elif abs_diff > 1600:
        score = 8
    elif abs_diff > 1400:
        score = 7
    elif abs_diff > 1200:
        score = 6
    elif abs_diff > 1000:
        score = 5
    elif abs_diff > 800:
        score = 4
    elif abs_diff > 600:
        score = 3
    elif abs_diff > 400:
        score = 2
    elif abs_diff > 200:
        score = 1
    else:
        score = 0  # 健康范围内

    return score


if __name__ == '__main__':
    # 示例用户数据
    weight = 70  # kg
    height = 175  # cm
    age = 30
    gender = 'male'

    # exercise_days_per_week = 7  # 用户每周运动天数，假设每天都有活动
    # 冗余参数

    average_calories_burned_per_week = 1400  # 用户每周平均运动消耗的卡路里
    # 实际应用中是读取过去7天的所有运动，然后计算这些运动的消耗卡路里总和

    calorie_intake = 2500  # 用户每日摄入卡路里
    calorie_burn = 200  # 用户每日通过运动消耗的卡路里

    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    score = calculate_score(tdee, calorie_intake, calorie_burn)

    print(f"BMR: {bmr:.2f}, TDEE: {tdee:.2f}, Score: {score:.2f}")
