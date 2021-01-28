import request from "./request";

export const OwnerAPI = {
    post: async (data) => {
        const response = await request.post(`/api/owners`, data, {
            withCredentials: true,
        });
        return response;
    },
    get: async (id) => {
        const response = await request.get(`/api/owners/${id}`, {
            withCredentials: true,
        });
        return response.data;
    },
    getByCondition: async (params, page = 0, size=10) => {
        const response = await request.get(`/api/owners`, {
            params: {
                ...params,
                page: page,
                size: size
            },
            withCredentials: true,
        });
        return response.data;
    },
    getAll: async (number) => {
        const response = await request.get(`/api/owners/all`, {
            params: {
                page: number,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },
    put: async (data) => {
        const response = await request.put(`/api/owners`, data, {
            withCredentials: true
        })
        return response;
    },
    delete: async (id) => {
        await request.delete(`/api/owners/${id}`, {
            withCredentials: true
        })
    },
}

