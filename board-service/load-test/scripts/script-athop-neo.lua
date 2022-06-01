local counter = 1
local upper_x = 50599
local lower_x = 1

function request()
	local i = getCounter()
	local n0 = getNextUpper()
	local n1 = getNextLower()
	path = "/athopNeo4j?nodes=" .. n0 .. "&nodes=" .. n1 .. "&types=TAXI&types=BUS&types=TAXI"
	wrk.headers["nodes"] = { n0, n1 }
	wrk.headers["types"] = { "TAXI", "BUS", "TAXI" }
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
	if upper_x == 1 then upper_x = 50599
	else upper_x = upper_x - 1
	end
	return upper_x
end

function getNextLower()
	if lower_x == 50599 then lower_x = 1
	else lower_x = lower_x + 1
	end
	return lower_x
end