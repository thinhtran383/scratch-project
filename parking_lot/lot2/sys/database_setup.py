from sqlalchemy import create_engine, Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import datetime

DATABASE_URL = 'sqlite:///parking_sys.db'
engine = create_engine(DATABASE_URL)
Base = declarative_base()


class User(Base):
    __tablename__ = 'users'
    
    id = Column(Integer, primary_key=True, autoincrement=True)
    username = Column(String(50), unique=True, nullable=False)
    password = Column(String(100), nullable=False)

class Checking(Base):
    __tablename__ = 'checking'
    
    id = Column(String(10000), primary_key=True)
    rfid_code = Column(String(50), unique=True, nullable=True)  
    license_plate = Column(String(50), nullable=False)         
    check_in_time = Column(DateTime, default=datetime.datetime.utcnow)  
    check_out_time = Column(DateTime, nullable=True)                   
    image_path = Column(String(100), nullable=True)                   

def create_tables():
    Base.metadata.create_all(engine)
    print("Đã tạo các bảng trong cơ sở dữ liệu.")

if __name__ == "__main__":
    create_tables()
