from datetime import datetime

# 吃饭时间得分
def calculate_mealtime_score(meal_times):
    ideal_times = {
        'breakfast': ('07:00', '09:00'),
        'lunch': ('12:00', '14:00'),
        'dinner': ('18:00', '20:00'),
    }
    score = 0
    
    def time_difference(time1, time2):
        # 计算两次之间的绝对小时差
        time_format = '%H:%M'
        datetime1 = datetime.strptime(time1, time_format)
        datetime2 = datetime.strptime(time2, time_format)
        difference = abs((datetime2 - datetime1).total_seconds() / 3600.0)
        return difference

    def is_within_time_range(actual_time, start, end):
        # 检查实际时间是否在 [start, end] 范围内
        time_format = '%H:%M'
        actual_dt = datetime.strptime(actual_time, time_format)
        start_dt = datetime.strptime(start, time_format)
        end_dt = datetime.strptime(end, time_format)
        return start_dt <= actual_dt <= end_dt

    for meal, actual_time in meal_times.items():
        ideal_start, ideal_end = ideal_times[meal]
        
        if is_within_time_range(actual_time, ideal_start, ideal_end):
            score += 10  # 满分
        else:
            # 计算理想时间开始或结束的最小差异
            difference_start = time_difference(actual_time, ideal_start)
            difference_end = time_difference(actual_time, ideal_end)
            min_difference = min(difference_start, difference_end)
            
            # 根据离理想时间范围的远近调整分数，最低分数为1
            if min_difference > 2.5:
                score += 1  # 距离超过 2.5 小时 1 分
            else:
                score += max(1, 10 - (min_difference / 0.25))  # 根据与理想时间的距离逐渐降低分数

    total_meals = len(meal_times)
    mealtime_score = score / total_meals if total_meals > 0 else 0  # Average score per meal

    return mealtime_score


# 餐食次数得分
def calculate_meal_count_score(meal_times):
    total_meals = len(meal_times)
    if total_meals == 0:
        # 没有任何餐食数据时，分数为0分
        return 0
    else:
        # 满分为10分，每缺少一餐扣3.33分
        score = max(0, 10 - (3 - total_meals) * 3.33)
        return score


# 卡路里消耗/摄入得分
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

def get_calories_difference_score(weight, height, age, gender, average_calories_burned_per_week,calorie_intake, calorie_burn):
    bmr = calculate_bmr(weight, height, age, gender)
    tdee = calculate_tdee(bmr, average_calories_burned_per_week)
    score = calculate_score(tdee, calorie_intake, calorie_burn)
    return score
    


# 计算总分
def get_diet_score(meal_times, weight, height, age, gender, average_calories_burned_per_week,calorie_intake, calorie_burn):
    time_score = calculate_mealtime_score(meal_times)
    count_score = calculate_meal_count_score(meal_times)
    calorie_score = get_calories_difference_score(weight, height, age, gender, average_calories_burned_per_week,calorie_intake, calorie_burn)
    total_diet_score = (calorie_score * 0.5) + (time_score * 0.3) + (count_score * 0.2)
    return total_diet_score


# 示例用户
weight = 70  # kg
height = 175  # cm
age = 30
gender = 'male'

average_calories_burned_per_week = 1400  # 用户每周平均运动消耗的卡路里

calorie_intake = 2500  # 用户每日摄入卡路里
calorie_burn = 200  # 用户每日通过运动消耗的卡路里

meal_times = {
    'breakfast': '08:00',
    'lunch': '13:00',
    'dinner': '20:00',
}

fin_score = get_diet_score(meal_times, weight, height, age, gender, average_calories_burned_per_week,calorie_intake, calorie_burn)

print(f"Diet Score: {fin_score:.2f}")


