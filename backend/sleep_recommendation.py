def generate_sleep_advice(sleep_score, sleep_start_time_variance, sleep_times_per_day, sleep_duration):
    actions = {
        "nap in a day, sleep score less than 2": "Eliminate napping during the day to ensure a more consolidated and restorative night's sleep.",
        "nap in a day, sleep score between 2 and 4": "Reduce your daytime napping by 50% to improve the quality and continuity of your nighttime sleep.",
        "nap in a day, sleep score between 4 and 6": "Reduce your daytime napping by 30% to help enhance nighttime sleep quality and duration.",
        "nap in a day, sleep score between 6 and 8": "Consider reducing daytime napping by 15% if it affects your ability to fall asleep or stay asleep at night.",
        "nap in a day, sleep score between 8 and 10": "Your sleep quality appears to be good. If you nap during the day and it does not affect your nighttime sleep, you may continue as is. However, if you find your nighttime sleep quality decreasing, consider adjusting your napping habits." ,

        "duration less than 6 hours, sleep score less than 2": "Engage in meditation or relaxation exercises before bedtime to improve the quality of your sleep and increase its duration.",
        "duration less than 6 hours, sleep score between 2 and 4": "Consider adjusting your evening routine to include winding down activities, such as reading or taking a warm bath, to extend your sleep duration by at least 30 minutes.",
        "duration less than 6 hours, sleep score between 4 and 6": "Review and optimize your sleep environment for comfort and tranquility, aiming to add an extra 20 minutes to your nightly sleep.",
        "duration less than 6 hours, sleep score between 6 and 8": "Ensure your sleeping area is dark, quiet, and cool. Experiment with slight changes, such as different bedding or sleepwear materials, to find what best extends your sleep duration.",
        "duration less than 6 hours, sleep score between 8 and 10": "Maintain a consistent sleep schedule, even on weekends, to regulate your body's clock and gradually increase your sleep duration. If you consistently sleep less than 6 hours but feel rested, ensure the quality of your sleep remains high.",

        "start time variance greater than 3 hours, sleep score less than 2": "Establish a consistent sleep schedule by going to bed and waking up at the same time every day, even on weekends, to regulate your body's internal clock.",
        "start time variance greater than 3 hours, sleep score between 2 and 4": "Begin by reducing your sleep start time variance by half. Aim for a more consistent bedtime within a 1.5-hour window to help stabilize your sleep pattern.",
        "start time variance greater than 3 hours, sleep score between 4 and 6": "Gradually adjust your bedtime to achieve a more consistent sleep schedule, with a goal of limiting the start time variance to within 1 hour.",
        "start time variance greater than 3 hours, sleep score between 6 and 8": "Monitor your sleep habits and gently adjust your bedtime to decrease the variance, striving for consistency in your sleep schedule.",
        "start time variance greater than 3 hours, sleep score between 8 and 10": "Your sleep quality is commendable. However, to further enhance your sleep pattern, consider fine-tuning your sleep schedule to minimize variance and promote a stable circadian rhythm."
    }
    advice = []
    if sleep_times_per_day >= 1.5 and sleep_score <= 2:
        advice.append(actions["nap in a day, sleep score less than 2"])
    elif sleep_times_per_day >= 1.5 and sleep_score <= 4:
        advice.append(actions["nap in a day, sleep score between 2 and 4"])
    elif sleep_times_per_day >= 1.5 and sleep_score <= 6:
        advice.append(actions["nap in a day, sleep score between 4 and 6"])
    elif sleep_times_per_day >= 1.5 and sleep_score <= 8:
        advice.append(actions["nap in a day, sleep score between 6 and 8"])
    elif sleep_times_per_day >= 1.5 and sleep_score <= 10:
        advice.append(actions["nap in a day, sleep score between 8 and 10"])
    if sleep_duration <= 6 and sleep_duration <= 2:
        advice.append(actions["duration less than 6 hours, sleep score less than 2"])
    elif sleep_duration <= 6 and sleep_duration <= 4:
        advice.append(actions["duration less than 6 hours, sleep score between 2 and 4"])
    elif sleep_duration <= 6 and sleep_duration <= 6:
        advice.append(actions["duration less than 6 hours, sleep score between 4 and 6"])
    elif sleep_duration <= 6 and sleep_duration <= 8:
        advice.append(actions["duration less than 6 hours, sleep score between 6 and 8"])
    elif sleep_duration <= 6 and sleep_duration <= 10:
        advice.append(actions["duration less than 6 hours, sleep score between 8 and 10"])
    if sleep_start_time_variance >= 3 and sleep_score <= 2:
        advice.append(actions["start time variance greater than 3 hours, sleep score less than 2"])
    elif sleep_start_time_variance >= 3 and sleep_score <= 4:
        advice.append(actions["start time variance greater than 3 hours, sleep score between 2 and 4"])
    elif sleep_start_time_variance >= 3 and sleep_score <= 6:
        advice.append(actions["start time variance greater than 3 hours, sleep score between 4 and 6"])
    elif sleep_start_time_variance >= 3 and sleep_score <= 8:
        advice.append(actions["start time variance greater than 3 hours, sleep score between 6 and 8"])
    elif sleep_start_time_variance >= 3 and sleep_score <= 10:
        advice.append(actions["start time variance greater than 3 hours, sleep score between 8 and 10"])

    advice_string = ", ".join(advice)
    return advice_string