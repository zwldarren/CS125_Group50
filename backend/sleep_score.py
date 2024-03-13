import numpy as np


def calculate_sleep_score(sleep_data: dict, sleep_model) -> int | None:
    duration = int(sleep_data["duration"])
    rem_sleep = float(sleep_data["stagePercentages"]["6"])
    deep_sleep = float(sleep_data["stagePercentages"]["5"])

    X_new = np.array([[duration, rem_sleep, deep_sleep]])
    predicted_score = sleep_model.predict(X_new)

    return int(predicted_score[0])
