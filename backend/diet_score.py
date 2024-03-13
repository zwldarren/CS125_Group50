from datetime import datetime


# 吃饭时间得分
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


# 餐食次数得分
def calculate_meal_count_score(meal_times):
    if not meal_times:
        return 0  # 0分如果没有数据

    total_meals = len(set(meal_times))  # 使用set去重 多次相同餐点时间只计算一次
    if total_meals >= 3:
        return 10
    else:
        # 每缺少一餐扣3.33分，最少为0分
        return max(0.0, 10.0 - (3 - total_meals) * 3.33)


# 卡路里消耗/摄入得分
def calculate_bmr(weight, height, age, gender):
    if gender == 'male':
        bmr = (10 * weight + 6.25 * height - 5 * age + 5) * 1000
    else:  # female
        bmr = (10 * weight + 6.25 * height - 5 * age - 161) *1000
    return bmr


def calculate_tdee(bmr, average_calories_burned_per_week):
    # 考虑到运动手表的会记录包括轻微活动如步行，因此主要判断条件是日平均消耗卡路里
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

    return score


def get_calories_difference_score(weight, height, age, gender, average_calories_burned_per_week,
                                  calorie_intake, calorie_burn):
    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    score = calculate_score(tdee, calorie_intake, calorie_burn)
    return score


# 计算总分
def get_diet_score(meal_times, weight, height, age, gender, average_calories_burned_per_week,
                   calorie_intake, calorie_burn):
    time_score = calculate_mealtime_score(meal_times)
    count_score = calculate_meal_count_score(meal_times)
    calorie_score = get_calories_difference_score(weight, height, age, gender,
                                                  average_calories_burned_per_week, calorie_intake,
                                                  calorie_burn)
    total_diet_score = (calorie_score * 0.5) + (time_score * 0.3) + (count_score * 0.2)
    return total_diet_score


if __name__ == "__main__":
    # 示例用户数据
    weight = 70  # kg
    height = 175  # cm
    age = 30
    gender = 'male'

    average_calories_burned_per_week = 1400000  # 用户上周运动消耗的总卡路里
    calorie_intake = 2500000  # 用户每日摄入卡路里
    calorie_burn = 200000  # 用户每日通过运动消耗的卡路里

    meal_times = ['08:00', '13:00', '20:00', '20:30', '18:30']

    fin_score = get_diet_score(meal_times, weight, height, age, gender,
                               average_calories_burned_per_week, calorie_intake, calorie_burn)

    print(f"Diet Score: {fin_score:.2f}")

