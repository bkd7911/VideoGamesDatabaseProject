import asyncio
import time

import psycopg2
import uvicorn
from fastapi import FastAPI, BackgroundTasks, Response, Form


app = FastAPI()


global driver

global count
count = 0

@app.get("/")
async def root():
    global count
    count += 1
    return {"message": f"sam look it works {count} times"}


@app.get("/create_user/{username}")
async def create_user():
    conn = psycopg2.connect(database="datacamp_courses",
                            user="datacamp",
                            host='localhost',
                            password="postgresql_tutorial",
                            port=5432)

    cur = conn.cursor()
    # Execute a command: create datacamp_courses table
    cur.execute("""INSERT INTO users (username, password) VALUES ('username', '')
                """)


    # Make the changes to the database persistent
    conn.commit()
    # Close cursor and communication with the database
    cur.close()
    conn.close()
    return {"message": f"sam look it works {count} times"}

@app.get("/{name}")
async def say_hello(name: str):
    global count
    count += 1
    return {"message": f"{name} look it works {count} times"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080)

#TODO make this work
