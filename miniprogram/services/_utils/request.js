function sendRequest(url, method, data) {
    return new Promise(function(resolve, reject) {
        var xhr = new XMLHttpRequest();
        xhr.open(method, url);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response);
            } else {
                reject(new Error(xhr.statusText));
            }
        };

        xhr.onerror = function() {
            reject(new Error('Network error'));
        };

        xhr.send(JSON.stringify(data));
    });
}

export function request(url, method, data) {
    const history = useHistory();

    return sendRequest(url, method, data)
        .then(function(response) {
            var responseData = JSON.parse(response);

            if (responseData.statusCode === 401) {
                history.push('/login');
                return Promise.reject(new Error('Unauthorized'));
            }

            // 其他状态码处理逻辑...

            return responseData;
        })
        .catch(function(error) {
            console.error('Request failed:', error);
            throw error;
        });
}