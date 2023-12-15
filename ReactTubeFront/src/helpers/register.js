export const register = async (user) => {
    return await fetch("http://localhost:8080/user", {
            method: 'POST',
            body: JSON.stringify(user),
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*'
            },
            mode: "cors"
        })
        .then(res => {
            if(!res.ok){
                return false;
            }
            return res.json();
        })
        .then(json => {
            return json;
        });
}

export default register;