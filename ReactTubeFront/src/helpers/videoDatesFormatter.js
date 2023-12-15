import { formatDistance } from "date-fns";

export const getVideoTotalVisits = (videoVisits) => {
  return videoVisits.length;
};

export const durationFormatter = (durationInSeconds) => {
  if (durationInSeconds < 3600) {
    return new Date(durationInSeconds * 1000).toISOString().substring(14, 19);
  } else {
    return new Date(durationInSeconds * 1000).toISOString().substring(11, 16);
  }
};

export const getUploadDateFormatted = (uploadDate) => {
  const from = new Date(uploadDate);
  const to = new Date();

  console.log(from);

  const distance = formatDistance(from, to, { addSuffix: true });

  //Capitalizing the first letter
  const distanceArray = distance.split("");
  distanceArray[0] = distanceArray[0].toUpperCase();

  return distanceArray.join("");
};
