import request from "./request";

export const SettleAPI = {
    post: async (data) => {
        const response = await request.post(`/api/settles`, data, {
            withCredentials: true,
        });
        return response;
    },
    registerBatch: async (data) => {
        const response = await request.post(`/api/settles/batch`, data, {
            withCredentials: true,
        });
        return response;
    },
    getByCondition: async (params, page = 0) => {
        const response = await request.get(`/api/settles`, {
            params: {
                ...params,
                page: page,
                size: 10
            },
            withCredentials: true,
        });
        return response.data;
    },
    getAll: async (params, page = 0) => {
        const response = await request.get(`/api/settles/all`, {
            params: {
                ...params,
                page: page,
                size: 10,
            },
            withCredentials: true,
        });
        return response.data;
    },
    getAmount: async (params) => {
        const response = await request.get(`/api/settles/amount`, {
            params: params,
            withCredentials: true,
        });
        return response.data;
    },
    patchStatus: async (data) => {
        const response = await request.patch(`/api/settles`, data, {
            withCredentials: true
        })
        return response;
    },
    updateBulkWithCondition: async (data) => {
        await request.patch(`/api/settles/bulk`, data, {
            withCredentials: true
        })
    },
    delete: async (id) => {
        await request.delete(`/api/settles/${id}`, {
            withCredentials: true
        })
    },
}
