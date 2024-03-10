from fastapi import FastAPI
import data

app = FastAPI()

# Run the server with the following command
# uvicorn main:app --reload

@app.post("/healthData/synchronize")
async def synchronize_health_data(healthData: data.HealthData):
    print(healthData)
    return {"message": "Hello World"}
