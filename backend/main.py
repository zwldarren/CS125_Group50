from fastapi import FastAPI
import data

app = FastAPI()


@app.post("/healthData/synchronize")
async def synchronize_health_data(healthData: data.HealthData):
    print(healthData)
    return {"message": "Hello World"}
