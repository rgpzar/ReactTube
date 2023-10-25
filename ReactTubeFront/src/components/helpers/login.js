export const getAuthSession = async (userDetails) => {
    try{
        const response = await fetch("http://localhost:8080/auth/login", {
            method: 'POST',
            body: JSON.stringify(userDetails),

            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*'
            },
            mode: "cors"
        });

        if(!response.ok) throw new Error("Invalid username or password");

        const jwt = await response.json();
        
        return jwt;
    }catch(e){
        console.error(e);
    }
} 

export default getAuthSession;