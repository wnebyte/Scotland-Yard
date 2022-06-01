local counter = 1
local upper_x = 55999
local lower_x = 1

function request()
	local i = getCounter()
	local x = getNextUpper()
	local y = getNextLower()
	path = "/shortestPathNeo4j?x=" .. x .. "&y=" .. y
	wrk.headers["x"] = x
	wrk.headers["y"] = y
	counter = i + 1
	return wrk.format(nil, path)
end

function response(status, headers, body)
	--print(status .. " | " .. body)
end

function getCounter()
	if counter >= 56000 then counter = 1 end
	return counter
end

function getNextUpper()
	if upper_x == 1 then upper_x = 55999
	else upper_x = upper_x - 1
	end
	return upper_x
end

function getNextLower()
	if lower_x == 55999 then lower_x = 1
	else lower_x = lower_x + 1
	end
	return lower_x
end
