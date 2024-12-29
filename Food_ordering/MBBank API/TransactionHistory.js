const axios = require("axios");

const apiUrl =
	"https://online.mbbank.com.vn/api/retail-web-transactionservice/transaction/getTransactionAccountHistory";
const payload = {
	accountNo: "0857723969",
	fromDate: "26/09/2023",
	toDate: "26/09/2023",
	sessionId: "4fcb91b6-83aa-4335-b551-ecc8e5dffe00",
	refNo: "VANANHLU-2023091607010697",
	deviceIdCommon: "oankw8vh-mbib-0000-0000-2023090618002619",
};

const headers = {
	Authorization:
		"Basic RU1CUkVUQUlMV0VCOlNEMjM0ZGZnMzQlI0BGR0AzNHNmc2RmNDU4NDNm",
	"Content-Type": "application/json",
};

axios
	.post(apiUrl, payload, { headers })
	.then(response => {
		console.log("Kết quả trả về:", response.data);
	})
	.catch(error => {
		console.error("Lỗi:", error);
	});
