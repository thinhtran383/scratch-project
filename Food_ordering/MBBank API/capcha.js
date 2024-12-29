const axios = require("axios");

const apiUrl =
	"https://online.mbbank.com.vn/api/retail-web-internetbankingms/getCaptchaImage";

const axiosInstance = axios.create({
	baseURL: apiUrl,
	headers: {
		Authorization:
			"Basic RU1CUkVUQUlMV0VCOlNEMjM0ZGZnMzQlI0BGR0AzNHNmc2RmNDU4NDNm",
		"Content-Type": "application/json; charset=UTF-8",
	},
});

const jsonData = {
	refNo: "2023091400484893",
	deviceIdCommon: "oankw8vh-mbib-0000-0000-2023090618002619",
	sessionId: "",
};

axiosInstance
	.post("", jsonData)
	.then(response => {
		console.log("Phản hồi từ máy chủ:", response.data);
	})
	.catch(error => {
		console.error("Lỗi khi gửi yêu cầu:", error);
	});
