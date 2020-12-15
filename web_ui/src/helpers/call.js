export default async (url, options = {}) => {

    if (options.body) {
        options.body = JSON.stringify(options.body);
    }

    options.headers = {
        ...options.headers || {},
        'Content-Type': 'application/json;charset=utf-8'
    };

    try {
        const response = await fetch(`${process.env.REACT_APP_API_BASE_URL}${url}`, options);

        const body = await response.json();

        if (response.status < 400) {
            return body;
        } else {
            throw new Error(body);
        }
    
    } catch (e) {
        throw e;
    }
};