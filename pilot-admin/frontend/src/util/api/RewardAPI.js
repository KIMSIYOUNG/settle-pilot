import request from "./request";

export const RewardAPI = {
    post: async (data) => {
        const response = await request.post(`/api/rewards`, data, {
            withCredentials: true,
        });
        return response;
    },
    postPeriodReward: async (data) => {
        const response = await request.post(`/api/rewards/period`, data, {
            withCredentials: true,
        });
        return response;
    },
    get: async (id) => {
        const response = await request.get(`/api/rewards/${id}`, {
            withCredentials: true,
        });
        return response.data;
    },
    getBySettleId: async (id, page) => {
        const response = await request.get(`/api/rewards/settles/${id}`, {
            params: {
                page: page,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },
    getAll: async (page) => {
        const response = await request.get(`/api/rewards/all`, {
            params: {
                page: page,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },
    getByCondition: async (params, page = 0) => {
        const response = await request.get(`/api/rewards/`, {
            params: {
                ...params,
                page: page,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },
    put: async (data) => {
        const response = await request.put(`/api/rewards`, data, {
            withCredentials: true
        })
        return response;
    },
    delete: async (id) => {
        await request.delete(`/api/rewards/${id}`, {
            withCredentials: true
        })
    },
}
