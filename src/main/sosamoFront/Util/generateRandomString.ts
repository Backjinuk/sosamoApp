export default function generateRandomString(length: number) {
  const characters =
    'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  const charactersLength = characters.length;

  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
}

export function RNG(length: number): number {
  const characters = '0123456789';
  let result = '';
  const charactersLength = characters.length;

  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return Number(result);
}

export function randomUUID() {
  function _s4() {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
  }
  return (
    _s4() +
    _s4() +
    '-' +
    _s4() +
    '-' +
    _s4() +
    '-' +
    _s4() +
    '-' +
    _s4() +
    _s4() +
    _s4()
  );
}
