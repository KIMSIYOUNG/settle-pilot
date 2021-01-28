import request from "./request";

export const MemberAPI = {
    get: async () => {
        const response = await request.get(`/api/members`, {
            withCredentials: true,
        });
        return response.data;
    },

    getAll: async (params, page = 0) => {
        const response = await request.get(`/api/members/all`, {
            params: {
                ...params,
                page: page,
                size: 10
            },
            withCredentials: true
        })
        return response.data;
    },

    patchRole: async (data) => {
        await request.patch(`/api/members/role`, data, {
            withCredentials: true,
        })
    },

    deleteByCurrentMember: async () => {
        await request.delete(`/api/members`, {
            withCredentials: true,
        })
    },

    deleteByAdmin: async (target) => {
        await request.delete(`/api/members/${target}`, {
            withCredentials: true,
        })
    },
}