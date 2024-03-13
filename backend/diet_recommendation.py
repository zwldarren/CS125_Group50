#enough_info 是Boolean, 其他都是int, 这些都是提取用户过去五天，然后得出, duration为hour，start_time_variance也为hour
def generate_diet_advice(diet_score, enough_info, calorie_difference, meal_times):
    actions = {
        "no enough data": "Start tracking your meals to get insights into your eating patterns.",

        "calorie surplus, diet score less than 2": "Swap sugary drinks for water infused with lemon or cucumber for hydration without extra calories.",
        "calorie surplus, diet score between 2 and 4": "Include berries, such as strawberries or blueberries, and leafy greens like spinach in your meals for nutrients without too many calories.",
        "calorie surplus, diet score between 4 and 6": "Incorporate grilled chicken or tofu into your meals for lean protein that keeps you feeling full longer.",
        "calorie surplus, diet score between 6 and 8": "Opt for fish like salmon or mackerel in your meals for omega-3 fatty acids, which support heart health without excessive calories.",
        "calorie surplus, diet score between 8 and 10": "Choose complex carbohydrates like lentils and chickpeas that provide sustained energy and keep you full, helping to manage calorie intake.",

        "calorie deficit, diet score less than 2": "Add avocados to your breakfast or snacks for healthy fats that boost calorie intake in a nutritious way.",
        "calorie deficit, diet score between 2 and 4": "Snack on a handful of almonds or walnuts to increase your calorie intake with heart-healthy fats.",
        "calorie deficit, diet score between 4 and 6": "Introduce quinoa or sweet potatoes into your meals for a wholesome source of carbohydrates and fiber.",
        "calorie deficit, diet score between 6 and 8": "Incorporate smoothies made with Greek yogurt or protein powder to increase your calorie intake with high-quality protein.",
        "calorie deficit, diet score between 8 and 10": "Enjoy meals with lean red meats or skin-on poultry to increase your intake of proteins and healthy fats, aiding in meeting your caloric needs.",

        "diet balance, diet score less than 2": "Consider a major overhaul of your diet. Start with breakfast, opting for oatmeal topped with fresh berries and a side of Greek yogurt to kickstart your day with fiber, antioxidants, and protein.",
        "diet balance, diet score between 2 and 4": "Diversify your protein sources by including more fish, beans, and legumes in your meals. For snacks, replace chips or sweets with nuts or sliced vegetables with hummus.",
        "diet balance, diet score between 4 and 6": "Balance your plate by filling half with vegetables, a quarter with lean proteins such as chicken or tofu, and a quarter with whole grains like brown rice or quinoa.",
        "diet balance, diet score between 6 and 8": "Fine-tune your diet by incorporating a variety of colorful fruits and vegetables. Try new types each week to ensure a wide range of vitamins and minerals.",
        "diet balance, diet score between 8 and 10": "Maintain your excellent diet by continuing to choose whole, unprocessed foods. Experiment with herbs and spices for flavoring, reducing the need for salt or sugar."
    }

    advice = []

    if not enough_info:
        advice.append(actions["no enough data"])

    if calorie_difference > 2800:  # Assuming a surplus of more than 500 calories is significant
        if diet_score <= 2:
            advice.append(actions["calorie surplus, diet score less than 2"])
        elif diet_score <= 4:
            advice.append(actions["calorie surplus, diet score between 2 and 4"])
        elif diet_score <= 6:
            advice.append(actions["calorie surplus, diet score between 4 and 6"])
        elif diet_score <= 8:
            advice.append(actions["calorie surplus, diet score between 6 and 8"])
        elif diet_score <= 10:
            advice.append(actions["calorie surplus, diet score between 8 and 10"])
    elif calorie_difference < -2800:  # Assuming a deficit of more than 500 calories
        if diet_score <= 2:
            advice.append(actions["calorie deficit, diet score less than 2"])
        elif diet_score <= 4:
            advice.append(actions["calorie deficit, diet score between 2 and 4"])
        elif diet_score <= 6:
            advice.append(actions["calorie deficit, diet score between 4 and 6"])
        elif diet_score <= 8:
            advice.append(actions["calorie deficit, diet score between 6 and 8"])
        elif diet_score <= 10:
            advice.append(actions["calorie deficit, diet score between 8 and 10"])
    else:
        if diet_score <= 2:
            advice.append(actions["diet balance, diet score less than 2"])
        elif diet_score <= 4:
            advice.append(actions["diet balance, diet score between 2 and 4"])
        elif diet_score <= 6:
            advice.append(actions["diet balance, diet score between 4 and 6"])
        elif diet_score <= 8:
            advice.append(actions["diet balance, diet score between 6 and 8"])
        elif diet_score <= 10:
            advice.append(actions["diet balance, diet score between 8 and 10"])

    if not is_time_in_range(meal_times['breakfast'], '07:00', '09:00'):
        advice.append("Consider having breakfast between 7:00 AM and 9:00 AM to kickstart your metabolism.")
    if not is_time_in_range(meal_times['lunch'], '12:00', '14:00'):
        advice.append("Aim to have lunch between 12:00 PM and 2:00 PM to maintain energy levels throughout the day.")
    if not is_time_in_range(meal_times['dinner'], '18:00', '20:00'):
        advice.append("Eating dinner between 6:00 PM and 8:00 PM can help with digestion and sleep quality.")

    advice_string = ", ".join(advice)
    return advice_string

def is_time_in_range(meal_time, start, end):
    """check if meal time is in range"""
    meal_hour = int(meal_time.split(':')[0])
    start_hour = int(start.split(':')[0])
    end_hour = int(end.split(':')[0])
    return start_hour <= meal_hour <= end_hour
