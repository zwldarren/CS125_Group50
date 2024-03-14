import requests


def synchronize_health_data(url, health_data):
    response = requests.post(url, json=health_data)
    return response.status_code, response.json()
