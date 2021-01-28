import request from "./request";

export const AuthorityAPI = {
    post: async (data) => {
        const response = await request.post(`/api/authorities`, data, {
            withCredentials: true,
        });
        return response;
    },
    approve: async (id) => {
        const response = await request.patch(`/api/authorities/approve/${id}`, null, {
            withCredentials: true
        })
        return response;
    },

    reject: async (id) => {
        const response = await request.patch(`/api/authorities/reject/${id}`, null, {
            withCredentials: true
        })
        return response;
    },
}

