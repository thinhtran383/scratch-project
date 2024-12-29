import sys

if sys.version_info >= (3, 12, 0):
    import six
    sys.modules['kafka.vendor.six.moves'] = six.moves


import streamlit as st
from streamlit_autorefresh import st_autorefresh
import json
import pandas as pd
import matplotlib.pyplot as plt
import os
from datetime import datetime

JSON_FILE_PATH = "vote_results.json"

if "votes" not in st.session_state:
    st.session_state.votes = {}

st.set_page_config(page_title="Real-Time Voting", layout="wide")
st.title("Real-Time Voting System")

last_refresh_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
st.markdown(f"##### Last refresh at: {last_refresh_time}")

st.markdown("---")

st.header("Vote for Your Favorite Candidate")
candidates = ["Candidate A", "Candidate B", "Candidate C", "Candidate D"]
selected_candidate = st.radio("Choose a candidate:", candidates)
if st.button("Submit Vote"):
    from kafka import KafkaProducer
    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        value_serializer=lambda v: json.dumps(v).encode("utf-8")
    )
    producer.send("voting", {"candidate": selected_candidate})
    st.success(f"Vote submitted for {selected_candidate}!")

st.markdown("---")

st.header("Live Voting Results")

st_autorefresh(interval=2000, limit=None, key="autorefresh")

if os.path.exists(JSON_FILE_PATH):
    with open(JSON_FILE_PATH, "r", encoding="utf-8") as f:
        st.session_state.votes = json.load(f)
else:
    st.write("Results file not found. Waiting for updates...")

votes = st.session_state.votes
if votes:
    df = pd.DataFrame(list(votes.items()), columns=["Candidate", "Votes"])

    total_votes = df["Votes"].sum()
    total_candidates = len(df)
    
    col1, col2 = st.columns([2, 3])

    with col1:
        st.markdown(f"**Total Candidates:** {total_candidates}")
    with col2:
        st.markdown(f"**Total Votes:** {total_votes}")
    
    st.markdown("---")

    leading_candidate = df.loc[df["Votes"].idxmax()]

    st.header("Leading Candidate")
    col1, col2 = st.columns([2, 3])
    with col1:
        st.markdown(f"**Name:** {leading_candidate['Candidate']}")
    with col2:
        st.markdown(f"**Votes:** {leading_candidate['Votes']}")

    st.dataframe(df)

    col1, col2 = st.columns(2)

    with col1:
        fig, ax = plt.subplots(figsize=(5, 5))
        ax.bar(df["Candidate"], df["Votes"], color="skyblue")
        ax.set_xlabel("Candidate")
        ax.set_ylabel("Votes")
        ax.set_title("Vote Count Distribution")
        
        ax.set_yticks(range(0, int(df["Votes"].max()) + 1, 1))
        
        st.pyplot(fig)

    with col2:
        fig, ax = plt.subplots(figsize=(5, 5))
        ax.pie(df["Votes"], labels=df["Candidate"], autopct="%1.1f%%")
        ax.set_title("Vote Distribution")
        st.pyplot(fig)

else:
    st.write("Waiting for votes...")
