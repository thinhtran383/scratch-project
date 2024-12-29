import tkinter as tk
from tkinter import ttk
import sqlite3

class ParkingManagementApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Parking Management")

        self.filter_label = tk.Label(self.root, text="Filter:")
        self.filter_label.grid(row=0, column=0, padx=10, pady=10)

        self.cb_filter = ttk.Combobox(self.root, values=["All", "In", "Out"])
        self.cb_filter.grid(row=0, column=1, padx=10, pady=10)
        self.cb_filter.current(0)
        self.cb_filter.bind("<<ComboboxSelected>>", self.on_filter_change)

        self.btn_refresh = tk.Button(self.root, text="Refresh", command=self.load_parking_records)
        self.btn_refresh.grid(row=0, column=2, padx=10, pady=10)

        self.tree = ttk.Treeview(self.root, columns=("id", "car_place", "rfid", "time_in", "time_out"), show="headings")
        self.tree.grid(row=1, column=0, columnspan=3, padx=10, pady=10)

        self.tree.heading("id", text="ID")
        self.tree.heading("car_place", text="Car Place")
        self.tree.heading("rfid", text="RFID")
        self.tree.heading("time_in", text="Time In")
        self.tree.heading("time_out", text="Time Out")

        self.tree.column("id", width=50, anchor=tk.CENTER)
        self.tree.column("car_place", width=150, anchor=tk.CENTER)
        self.tree.column("rfid", width=100, anchor=tk.CENTER)
        self.tree.column("time_in", width=200, anchor=tk.CENTER) 
        self.tree.column("time_out", width=200, anchor=tk.CENTER)  

        self.load_parking_records()

    def load_parking_records(self):
        conn = sqlite3.connect('car_parking.db')
        cursor = conn.cursor()

        filter_option = self.cb_filter.get()

        query = "SELECT id, car_place, rfid, time_in, time_out FROM parking_records"
        if filter_option == "In":
            query += " WHERE time_out IS NULL"
        elif filter_option == "Out":
            query += " WHERE time_out IS NOT NULL"

        cursor.execute(query)
        rows = cursor.fetchall()

        for item in self.tree.get_children():
            self.tree.delete(item)

        for row in rows:
            self.tree.insert("", tk.END, values=row)

        conn.close()

    def on_filter_change(self, event):
        self.load_parking_records()

# # Create the main window
root = tk.Tk()
app = ParkingManagementApp(root)

# Run the application
root.mainloop()
