import urequests

class ApiClient:
    def __init__(self):
        pass

    def send_request(self, url, method, body=None):
        try:
           
            if method.lower() == 'get':
                response = urequests.get(url)
            elif method.lower() == 'post':
                response = urequests.post(url, json=body)
            else:
                raise ValueError("Only GET and POST method.")

            
            if response.status_code == 201:
                json_response = response.json()
                return json_response.get('message', 'Not found messsage field')
            else:
                return f"Lỗi {response.status_code}: {response.text}"

        except Exception as e:
            return f"Lỗi khi gửi yêu cầu: {e}"
        finally:
            response.close() 

'''
# Sử dụng lớp ApiClient
if __name__ == "__main__":
    api_client = ApiClient()
    
    # Gửi yêu cầu GET
    

    # Gửi yêu cầu POST
    url_post = 'https://local.thinhtran.online/car-in'
    body = {"rfid_code":"12131111111"}
    message_post = api_client.send_request(url_post, 'post', body)
    print("Thông điệp từ POST:", message_post)
'''
