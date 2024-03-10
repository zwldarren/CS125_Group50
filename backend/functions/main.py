# Welcome to Cloud Functions for Firebase for Python!
# To get started, simply uncomment the below code or create your own.
# Deploy with `firebase deploy`

from firebase_functions import https_fn
from firebase_admin import initialize_app, credentials, firestore

initialize_app()


@https_fn.on_request()
def on_request_example(req: https_fn.Request) -> https_fn.Response:
    return https_fn.Response("Hello world!")


@https_fn.on_call()
def synchronizeHealthData(req: https_fn.CallableRequest) -> any:
    uid = req.auth.uid

    health_data = {
        "uid": uid,
        "sleepRecords_2": req.data["sleepRecords"],
        "exerciseRecords": req.data["exerciseRecords"],
        "dietRecords": req.data["dietRecords"],
        "heartRateRecords": req.data["heartRateRecords"],
        "totalCaloriesBurnedRecords": req.data["totalCaloriesBurnedRecords"],
        "activeCaloriesBurnedRecords": req.data["activeCaloriesBurnedRecords"],
    }
    
    return health_data
